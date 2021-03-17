package io.tackle.controls.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.DisabledOnNativeImage;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.controls.entities.JobFunction;
import io.tackle.controls.entities.Stakeholder;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.tackle.controls.entities.StakeholderGroup;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
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
        JobFunction consultant = new JobFunction();
        consultant.id = 7L;
        stakeholder.jobFunction = consultant;
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

    @Test
    @DisabledOnNativeImage
    public void testStakeholderCreateUpdateAndDeleteEndpoint() {
        testStakeholderCreateUpdateAndDeleteEndpoint(false);
    }

    protected void testStakeholderCreateUpdateAndDeleteEndpoint(boolean nativeExecution) {
        final String displayName = "Another Stakeholder displayName";
        final String description = "Another Stakeholder description";
        Stakeholder stakeholder = new Stakeholder();
        stakeholder.displayName = displayName;

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(stakeholder)
                .when().post(PATH)
                .then()
                .log().all()
                .statusCode(201).extract().response();

        TestCase.assertEquals(displayName, response.path("displayName"));
        TestCase.assertEquals("alice", response.path("createUser"));
        TestCase.assertEquals("alice", response.path("updateUser"));

        Long stakeholderId = Long.valueOf(response.path("id").toString());

        StakeholderGroup sg = new StakeholderGroup();
        String groupName = "Group 1";
        sg.name = groupName;
        sg.description = "Test Group";


        final String newName = "Yet another different displayName";
        stakeholder.displayName = newName;
        stakeholder.stakeholderGroups.add(sg);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(stakeholder)
                .pathParam("id", stakeholderId)
                .when().put(PATH + "/{id}")
                .then().statusCode(204);

        given()
                .accept("application/json")
                .pathParam("id", stakeholderId)
                .when().get(PATH + "/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("displayName", is(newName),
                        "stakeholderGroups.size()", is(1));

        if (!nativeExecution) {
            Stakeholder updatedStakeholderFromDb = Stakeholder.findById(stakeholderId);
            TestCase.assertEquals(newName, updatedStakeholderFromDb.displayName);
            assertNotNull(updatedStakeholderFromDb.createTime);
            assertNotNull(updatedStakeholderFromDb.updateTime);
        }

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
