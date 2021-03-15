package io.tackle.controls.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInRelativeOrder;
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
public class StakeholderGroupTest extends SecuredResourceTest {

    @BeforeAll
    public static void init() {
        PATH = "/stakeholder-group";
    }

    @Test
    public void testStakeholderGroupListEndpoint() {
        given()
                .accept("application/json")
                .queryParam("sort", "-stakeholders.size")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(3),
                        "id", containsInRelativeOrder(52, 53, 54),
                        "name", containsInRelativeOrder("Managers", "Engineers", "Marketing"),
                        "createUser", containsInRelativeOrder("<pre-filled>", "<pre-filled>", "<pre-filled>"),
                        "[0].stakeholders.size()", is(2)
                );
    }

    @Test
    public void testStakeholderGroupFilteredMultipleParamsListEndpoint() {
        given()
                .accept("application/json")
                .queryParam("name", "gers")
                .queryParam("description", "Man")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(1),
                        "id", contains(52),
                        "name", contains("Managers"),
                        "createUser", contains("<pre-filled>"),
                        "updateUser", contains("<pre-filled>")
                );
    }



    @Test
    public void testStakeholderGroupFilteredMultipleParamsWithMultipleValuesListEndpoint() {
        given()
                .accept("application/json")
                .queryParam("name", "gers")
                .queryParam("name", "edit")
                .queryParam("description", "Man")
                .queryParam("sort", "id")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(1),
                        "id", contains(52),
                        "name", contains("Managers"),
                        "[0].stakeholders.size()", is(2),
                        "[0].stakeholders.displayName", containsInRelativeOrder("Jessica Fletcher", "Emmett Brown"),
                        "createUser", contains("<pre-filled>"),
                        "updateUser", contains("<pre-filled>")
                );

        given()
                .accept("application/json")
                .queryParam("sort", "-id")
                .queryParam("description", "up")
                .queryParam("stakeholders.displayName", "met")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(2),
                        "id", containsInRelativeOrder(53, 52),
                        "name", containsInRelativeOrder("Engineers", "Managers"),
                        "createUser", containsInRelativeOrder("<pre-filled>", "<pre-filled>"),
                        "[0].stakeholders[0].displayName", is("Emmett Brown"),
                        "[1].stakeholders.displayName", containsInRelativeOrder("Jessica Fletcher", "Emmett Brown")
                );
    }

/*    @Test
    @DisabledOnNativeImage
    public void testStakeholderGroupCreateUpdateAndDeleteEndpoint() {
        testStakeholderGroupCreateUpdateAndDeleteEndpoint(false);
    }

    protected void testStakeholderGroupCreateUpdateAndDeleteEndpoint(boolean nativeExecution) {
        final String name = "Another StakeholderGroup name";
        final String description = "Another StakeholderGroup description";
        StakeholderGroup stakeholderGroup = new StakeholderGroup();
        stakeholderGroup.name = name;
        stakeholderGroup.description = description;

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(stakeholderGroup)
                .when().post(PATH)
                .then()
                .log().all()
                .statusCode(201).extract().response();

        assertEquals(name, response.path("name"));
        assertEquals(description, response.path("description"));
        assertEquals("alice", response.path("createUser"));
        assertEquals("alice", response.path("updateUser"));

        Long stakeholderGroupId = Long.valueOf(response.path("id").toString());

        final String newName = "Yet another different name";
        stakeholderGroup.name = newName;
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(stakeholderGroup)
                .pathParam("id", stakeholderGroupId)
                .when().put(PATH + "/{id}")
                .then().statusCode(204);

        given()
                .accept("application/json")
                .pathParam("id", stakeholderGroupId)
                .when().get(PATH + "/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is(newName),
                        "description", is(description));

        if (!nativeExecution) {
            StakeholderGroup updatedStakeholderGroupFromDb = StakeholderGroup.findById(stakeholderGroupId);
            assertEquals(newName, updatedStakeholderGroupFromDb.name);
            assertEquals(description, updatedStakeholderGroupFromDb.description);
            assertNotNull(updatedStakeholderGroupFromDb.createTime);
            assertNotNull(updatedStakeholderGroupFromDb.updateTime);
        }

        given()
                .pathParam("id", stakeholderGroupId)
                .when().delete(PATH + "/{id}")
                .then().statusCode(204);

        given()
                .accept("application/json")
                .pathParam("id", stakeholderGroupId)
                .when().get(PATH + "/{id}")
                .then()
                .log().all()
                .statusCode(404);

    }*/
}
