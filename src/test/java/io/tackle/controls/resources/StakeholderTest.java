package io.tackle.controls.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.tackle.controls.entities.Stakeholder;
import io.tackle.controls.testcontainers.keycloak.KeycloakResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.tackle.controls.testcontainers.postresql.PostgreSQLResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(PostgreSQLResource.class)
@QuarkusTestResource(KeycloakResource.class)
public class StakeholderTest extends SecuredResourceTest {
    
    @BeforeAll
    public static void init() {
        PATH = "/stakeholder";
    }

    @Test
    public void testStakeholdersListHalEndpoint() {
        given()
                .accept("application/hal+json")
                .queryParam("sort", "id")
                .when().get(PATH)
                .then()
                .statusCode(200)
                .body("_embedded.stakeholder.size()", is(2),
                        "_embedded.stakeholder.id", containsInRelativeOrder(4, 5),
                        "_embedded.stakeholder.displayName", containsInRelativeOrder("Jessica Fletcher", "Emmett Brown"),
                        "_embedded.stakeholder[1]._links.size()", is(5),
                        "_embedded.stakeholder[1]._links.self.href", is("http://localhost:8081/controls/stakeholder/5"),
                        "_links.size()", is(4));
    }

    @Test
    public void testBusinessServicesFilteredSingleParamListHalEndpoint() {
        given()
                .accept("application/hal+json")
                .queryParam("displayName", "sica")
                .when().get(PATH)
                .then()
                .statusCode(200)
                .body("_embedded.stakeholder.size()", is(1),
                        "_embedded.stakeholder.id", contains(4),
                        "_embedded.stakeholder.displayName", contains("Jessica Fletcher"),
                        "_embedded.stakeholder[0]._links.size()", is(5),
                        "_embedded.stakeholder[0]._links.self.href", is("http://localhost:8081/controls/stakeholder/4"),
                        "_links.size()", is(4));
    }

    @Test
    public void testBusinessServicesFilteredMultipleParamsListEndpoint() {
        given()
                .accept("application/json")
                .queryParam("displayName", "met")
                .queryParam("email", "greatscott")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(1),
                        "id", contains(5),
                        "displayName", contains("Emmett Brown"),
                        "createUser", contains("mrizzi")
                );
    }

    @Test
    public void testBusinessServicesFilteredMultipleParamsWithMultipleValuesListEndpoint() {
        given()
                .accept("application/json")
                .queryParam("displayName", "emme")
                .queryParam("displayName", "essi")
                .queryParam("email", "murder")
                .queryParam("email", "she")
                .queryParam("email", "wrote")
                .queryParam("sort", "-id")
//                .queryParam("stakeholder group ???", "met")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(1),
                        "id", contains(4),
                        "displayName", contains("Jessica Fletcher"),
                        "createUser", contains("mrizzi")
                );
    }

    @Test
    public void testStakeholdersListEndpoint() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.config
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> new ObjectMapper()
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule())
                        /*.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)*/));
        
        given()
                .accept("application/json")
                .queryParam("sort", "id")
                .when().get(PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(2),
                        "id", containsInRelativeOrder(4, 5),
                        "displayName", containsInRelativeOrder("Jessica Fletcher", "Emmett Brown"),
                        "createUser", containsInRelativeOrder("mrizzi", "mrizzi"),
                        "updateUser", containsInRelativeOrder(blankOrNullString(), blankOrNullString())
                );
    }

    @Test
    public void testStakeholderCreateAndDeleteEndpoint() {
        Stakeholder stakeholder = new Stakeholder();
        stakeholder.displayName = "John Smith";
        stakeholder.jobFunction = "CEO";
        stakeholder.email = "another@email.foo";

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(stakeholder)
                .when().post(PATH)
                .then()
                .log().all()
                .statusCode(201).extract().response();

        assertEquals("alice", response.path("createUser"));
        assertEquals("alice", response.path("updateUser"));

        Integer stakeholderId = response.path("id");

        given()
                .pathParam("id", stakeholderId)
                .when().delete(PATH + "/{id}")
                .then().statusCode(204);

        given()
                .accept("application/json")
                .pathParam("id", stakeholderId)
                .when().get(PATH + "/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }
}
