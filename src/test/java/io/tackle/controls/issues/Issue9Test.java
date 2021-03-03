package io.tackle.controls.issues;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.controls.entities.BusinessService;
import io.tackle.controls.entities.Stakeholder;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

// https://github.com/konveyor/tackle-controls/issues/9
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
public class Issue9Test extends SecuredResourceTest {

    @Test
    public void testIssue9() {
        Stakeholder owner = new Stakeholder();
        owner.displayName = "Owner";
        Response ownerResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(owner)
                .when().post("/stakeholder")
                .then()
                .log().all()
                .statusCode(201).extract().response();
        Long ownerId = Long.valueOf(ownerResponse.path("id").toString());

        BusinessService businessService = new BusinessService();
        businessService.name = "Business Service";
        businessService.owner = ownerResponse.as(Stakeholder.class);
        Response businessServiceResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(businessService)
                .when()
                .log().all()
                .post("/business-service")
                .then()
                .log().all()
                .statusCode(201).extract().response();
        Long businessServiceId = Long.valueOf(businessServiceResponse.path("id").toString());

        given()
                .accept("application/json")
                .queryParam("name", "business")
                .when().get("/business-service")
                .then()
                .log().all()
                .statusCode(200)
                .body("name[0]", is("Business Service"),
                        "owner[0].displayName", is("Owner"),
                        "id[0]", is(businessServiceId.intValue()),
                        "size()", is(1));

        given()
            .pathParam("id", ownerId)
            .when().delete("/stakeholder/{id}")
            .then().statusCode(204);

        given()
            .accept("application/json")
            .queryParam("name", "business")
            .when().get("/business-service")
            .then()
            .log().all()
            .statusCode(200)
            .body("name[0]", is("Business Service"),
                    "owner[0]", is(emptyOrNullString()),
                    "id[0]", is(businessServiceId.intValue()),
                    "size()", is(1));

        // let's remove also the Business Service to not interfere with other tests
        given()
                .pathParam("id", businessServiceId)
                .when().delete("/business-service/{id}")
                .then().statusCode(204);
    }
}
