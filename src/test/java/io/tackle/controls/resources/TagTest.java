package io.tackle.controls.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.controls.entities.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

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
public class TagTest extends SecuredResourceTest {

    @BeforeAll
    public static void init() {
        PATH = "/tag";
    }
    
    @Test
    public void testTagListByTagTypeHalEndpoint() {
        given()
                .accept("application/hal+json")
                .queryParam("sort", "name")
                .queryParam("tagType.id", "23")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("_embedded.tag.size()", is(7));
    }

    @Test
    // https://github.com/konveyor/tackle-controls/issues/101
    public void testCreateWithoutTagType() {
        Tag tag = new Tag();
        tag.name = "test";
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(tag)
                .when()
                .post(PATH)
                .then()
                // this will expect a '409' from Quarkus 1.13+ with the introduction of RestDataPanacheException
                .statusCode(500);
    }
}
