package io.tackle.controls.resources;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;

public abstract class SecuredResourceTest {

    private static final String KEYCLOAK_SERVER_URL = System.getProperty("quarkus.oidc.auth-server-url", "http://localhost:8180/auth");
    protected static String PATH = "";
    protected static String ACCESS_TOKEN;

    @BeforeAll
    public static void setUp() {
        ACCESS_TOKEN =
                given()
                        .relaxedHTTPSValidation()
                        .auth().preemptive().basic("backend-service", "secret")
                        .contentType("application/x-www-form-urlencoded")
//                        .log().all()
                        .formParam("grant_type", "password")
                        .formParam("username", "alice")
                        .formParam("password", "alice")
                        .when()
                        .post(KEYCLOAK_SERVER_URL + "/protocol/openid-connect/token")
                        .then().extract().path("access_token").toString();
        RestAssured.authentication = oauth2(ACCESS_TOKEN);
    }

    @Test
    public void testUnauthorized(){
        given().auth().oauth2("")
                .accept("application/hal+json")
                .queryParam("sort", "id")
                .when().get(PATH)
                .then()
                .statusCode(401);
    }
}
