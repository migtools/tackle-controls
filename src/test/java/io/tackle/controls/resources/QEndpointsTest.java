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
package io.tackle.controls.resources;

import io.quarkus.test.common.ResourceArg;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
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
@QuarkusTestResource(value = KeycloakTestResource.class,
        initArgs = {
                @ResourceArg(name = KeycloakTestResource.IMPORT_REALM_JSON_PATH, value = "keycloak/quarkus-realm.json"),
                @ResourceArg(name = KeycloakTestResource.REALM_NAME, value = "quarkus")
        }
)
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
