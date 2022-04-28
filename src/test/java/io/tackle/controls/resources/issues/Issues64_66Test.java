/*
 * Copyright © 2021 Konveyor (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.tackle.controls.resources.issues;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.controls.entities.BusinessService;
import io.tackle.controls.entities.JobFunction;
import io.tackle.controls.entities.Stakeholder;
import io.tackle.controls.entities.StakeholderGroup;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
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
public class Issues64_66Test extends SecuredResourceTest {

    @Test
    public void test() {
        // create a new job function
        final String role = "chef";
        final JobFunction chef = new JobFunction();
        chef.role = role;
        chef.id = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .body(chef)
                .when()
                .post("/job-function")
                .then()
                .statusCode(is(201))
                .extract()
                .path("id")
                .toString());

        // use the already existing stakeholder groups
        final StakeholderGroup managers = new StakeholderGroup();
        managers.id = 52L;
        final StakeholderGroup engineers = new StakeholderGroup();
        engineers.id = 53L;
        final StakeholderGroup marketing = new StakeholderGroup();
        marketing.id = 54L;

        // create a stakeholder with a job function
        final String stakeholderDisplayName = "Display Name";
        final Stakeholder stakeholder = new Stakeholder();
        stakeholder.displayName = stakeholderDisplayName;
        stakeholder.email = "x@y.z";
        // just inserted job function use case
        stakeholder.jobFunction = chef;
        stakeholder.stakeholderGroups = Set.of(managers, engineers, marketing);
        stakeholder.id = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(stakeholder)
                .when()
                .post("/stakeholder")
                .then()
                .statusCode(is(201))
                .extract()
                .path("id")
                .toString());

        // check the stakeholder has the job function
        // and the three stakeholder groups in the response
        given()
                .accept("application/hal+json")
                .queryParam("displayName", stakeholderDisplayName)
                .body(stakeholder)
                .when()
                .get("/stakeholder")
                .then()
                .statusCode(is(200))
                .body("_embedded.stakeholder[0].jobFunction.role", is(role),
                        "_embedded.stakeholder[0].stakeholderGroups.size()", is(3));
        
        // create a business service with owner the stakeholder just created 
        final String businessServiceName = "it works";
        final BusinessService businessService = new BusinessService();
        businessService.name = businessServiceName;
        businessService.description = "yes, it works";
        businessService.owner = stakeholder;
        businessService.id = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(businessService)
                .when()
                .post("/business-service")
                .then()
                .statusCode(is(201))
                .extract()
                .path("id")
                .toString());

        // check the business service has the owner in the response
        given()
                .accept(ContentType.JSON)
                .queryParam("name", businessServiceName)
                .body(stakeholder)
                .when()
                .get("/business-service")
                .then()
                .statusCode(is(200))
                .body("[0].owner.displayName", is(stakeholderDisplayName));

        // remove the owner from the business service
        businessService.owner = null;
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", businessService.id)
                .body(businessService)
                .when()
                .put("/business-service/{id}")
                .then()
                .statusCode(is(204));

        // check the business service has NO owner
        given()
                .accept(ContentType.JSON)
                .queryParam("name", businessServiceName)
                .body(stakeholder)
                .when()
                .get("/business-service")
                .then()
                .statusCode(is(200))
                .body("[0].owner.displayName", is(emptyOrNullString()),
                "[0].owner", is(emptyOrNullString()));

        // Issue#64: remove the job function from the stakeholder
        stakeholder.jobFunction = null;
        // Issue#66: remove 2 stakeholder groups
        stakeholder.stakeholderGroups = Set.of(managers);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", stakeholder.id)
                .body(stakeholder)
                .when()
                .put("/stakeholder/{id}")
                .then()
                .statusCode(is(204));

        // check the stakeholder has NO job function in the response
        given()
                .accept("application/hal+json")
                .queryParam("displayName", stakeholderDisplayName)
                .body(stakeholder)
                .when()
                .get("/stakeholder")
                .then()
                .statusCode(is(200))
                .body("_embedded.stakeholder[0].jobFunction.role", is(emptyOrNullString()),
                        "_embedded.stakeholder[0].stakeholderGroups.size()", is(1),
                        "_embedded.stakeholder[0].stakeholderGroups.size()", is(1),
                        "_embedded.stakeholder[0].stakeholderGroups[0].name", is("Managers"));

        // delete the resource created in this test to not interfere with other tests
        given()
                .pathParam("id", chef.id)
                .when()
                .delete("/job-function/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", stakeholder.id)
                .when()
                .delete("/stakeholder/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", businessService.id)
                .when()
                .delete("/business-service/{id}")
                .then()
                .statusCode(204);
    }
}
