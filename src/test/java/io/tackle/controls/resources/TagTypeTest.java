package io.tackle.controls.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.controls.entities.Tag;
import io.tackle.controls.entities.TagType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(value = PostgreSQLDatabaseTestResource.class,
        initArgs = {
                @ResourceArg(name = PostgreSQLDatabaseTestResource.DB_NAME, value = "controls_db"),
                @ResourceArg(name = PostgreSQLDatabaseTestResource.USER, value = "controls"),
                @ResourceArg(name = PostgreSQLDatabaseTestResource.PASSWORD, value = "controls")
        }
)
@QuarkusTestResource(value = KeycloakTestResource.class,
        initArgs = {
                @ResourceArg(name = KeycloakTestResource.IMPORT_REALM_JSON_PATH, value = "keycloak/quarkus-realm.json"),
                @ResourceArg(name = KeycloakTestResource.REALM_NAME, value = "quarkus")
        }
)
public class TagTypeTest extends SecuredResourceTest {

    @BeforeAll
    public static void init() {
        PATH = "/tag-type";
    }
    
    @Test
    public void testListHalEndpoint() {
        given()
                .accept("application/hal+json")
                .queryParam("sort", "name")
                .when().get(PATH)
                .then()
                .statusCode(200)
                .body("_embedded.tag-type.size()", is(6),
                        "_embedded.tag-type.name", containsInRelativeOrder("Application Type", "Database", "Data Center", "Language", "Operating System", "Runtime"));
    }
    
    @Test
    public void testFilteredListsHalEndpoint() {
        given()
                .accept("application/hal+json")
                .queryParam("sort", "-id")
                .queryParam("tags.name", "ark")
                .queryParam("tags.name", "rac")
                .when().get(PATH)
                .then()
                .statusCode(200)
                .body("_embedded.tag-type", iterableWithSize(2));
    }

    @Test
    protected void testCreateUpdateAndDeleteEndpoint() {
        final String name = "Nice tag";
        final int rank = 1;
        final String colour = "red";
        TagType tagType = new TagType();
        tagType.name = name;
        tagType.rank = rank;
        tagType.colour = colour;

        // create TagType
        Response tagTypeResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(tagType)
                .when().post(PATH)
                .then()
                .statusCode(201).extract().response();

        assertEquals(name, tagTypeResponse.path("name"));
        assertEquals(rank, (Integer)tagTypeResponse.path("rank"));
        assertEquals(colour, tagTypeResponse.path("colour"));
        assertEquals("alice", tagTypeResponse.path("createUser"));
        assertEquals("alice", tagTypeResponse.path("updateUser"));

        Long tagTypeId = Long.valueOf(tagTypeResponse.path("id").toString());

        // create a Tag associated to the TagType
        Tag firstTag = new Tag();
        firstTag.name = "first value";
        firstTag.tagType = tagTypeResponse.as(TagType.class);
        
        Response firstTagResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(firstTag)
                .when().post("/tag")
                .then()
                .statusCode(201).extract().response();

        assertEquals("first value", firstTagResponse.path("name"));
        assertEquals(tagTypeId.intValue(), (Integer)firstTagResponse.path("tagType.id"));

        Long firstTagId = Long.valueOf(firstTagResponse.path("id").toString());

        // create a Tag associated to the TagType
        Tag secondTag = new Tag();
        secondTag.name = "second value";
        secondTag.tagType = tagTypeResponse.as(TagType.class);
        
        Response secondTagResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(secondTag)
                .when().post("/tag")
                .then()
                .statusCode(201).extract().response();

        assertEquals("second value", secondTagResponse.path("name"));
        assertEquals(tagTypeId.intValue(), (Integer)secondTagResponse.path("tagType.id"));

        Long secondTagId = Long.valueOf(secondTagResponse.path("id").toString());

        // time to delete the TagType
        given()
                .pathParam("id", tagTypeId)
                .when().delete(PATH + "/{id}")
                .then().statusCode(204);
        // check it's not found anymore
        given()
                .accept("application/json")
                .pathParam("id", tagTypeId)
                .when().get(PATH + "/{id}")
                .then()
                .statusCode(404);

        // and now check also the Tags are not found anymore
        given()
                .accept(ContentType.JSON)
                .pathParam("id", firstTagId)
                .when().get("/tag/{id}")
                .then()
                .statusCode(404);

        given()
                .accept(ContentType.JSON)
                .pathParam("id", secondTagId)
                .when().get("/tag/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    // from https://github.com/konveyor/tackle-controls/issues/98
    public void testListSortedByRank() {
        TagType tagType = new TagType();
        tagType.name = "sort by rank test";
        tagType.rank = 100;
        tagType.colour = "#123456";

        tagType.id = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(tagType)
                .when().post(PATH)
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString());

        given()
                .accept("application/hal+json")
                .queryParam("sort", "-rank")
                .when().get(PATH)
                .then()
                .statusCode(200)
                .body("_embedded.tag-type.size()", is(7),
                        "_embedded.tag-type.name", containsInRelativeOrder("sort by rank test", "Application Type", "Data Center", "Database", "Runtime", "Operating System", "Language"));

        given()
                .pathParam("id", tagType.id)
                .when()
                .delete(PATH + "/{id}")
                .then()
                .statusCode(204);
    }
}
