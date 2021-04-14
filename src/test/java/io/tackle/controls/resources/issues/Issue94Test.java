package io.tackle.controls.resources.issues;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.controls.entities.Stakeholder;
import io.tackle.controls.entities.StakeholderGroup;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

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
public class Issue94Test extends SecuredResourceTest {
    @Test
    public void test() {
        Stakeholder stakeholder = new Stakeholder();
        stakeholder.displayName = "test stakeholder name";
        stakeholder.email = "x@y.z";

        StakeholderGroup managers = new StakeholderGroup();
        managers.id = 52L;
        stakeholder.stakeholderGroups.add(managers);
        StakeholderGroup engineers = new StakeholderGroup();
        engineers.id = 53L;
        stakeholder.stakeholderGroups.add(engineers);
        StakeholderGroup marketing = new StakeholderGroup();
        marketing.id = 54L;
        stakeholder.stakeholderGroups.add(marketing);

        stakeholder.id = Long.valueOf(
                given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(stakeholder)
                .when()
                        .log().body()
                .post("/stakeholder")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString());

        given()
                .accept("application/hal+json")
                .pathParam("id", stakeholder.id)
                .when()
                .get("/stakeholder/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .body("stakeholderGroups", is(notNullValue()),
                        "stakeholderGroups.size()", is(3));
    }
}
