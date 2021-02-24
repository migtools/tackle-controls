package io.tackle.controls.resources;

import io.tackle.controls.testcontainers.keycloak.KeycloakResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Test done to be sure the `/q/*` endpoints (e.g. metrics, health, etc)
 * are not secured and so do not require any authentication/authorization
 */
@QuarkusTest
// starting Keycloak to be sure it doesn't block request
@QuarkusTestResource(KeycloakResource.class)
public class QEndpointsTest extends SecuredResourceTest {

    @Test
    public void testQEndpoints() {
        given().auth().oauth2("")
                .accept("application/json")
                .when().get("/q/health/")
                .then()
                .statusCode(200);
    }
}
