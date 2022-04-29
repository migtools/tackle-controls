/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
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

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.controls.entities.Tag;
import io.tackle.controls.entities.TagType;
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
                // this will expect as well a '409' from Quarkus 1.13+ with the introduction of RestDataPanacheException
                .statusCode(409);
    }

    @Test
    // https://github.com/konveyor/tackle-controls/issues/114
    public void testSameNameWithDifferentTagTypes() {
        final String tagName = "test unique name";
        // create a tag associated with a tag type
        final Tag tag = new Tag();
        tag.name = tagName;
        final TagType tagType = new TagType();
        tagType.id = 20L;
        tag.tagType = tagType;
        tag.id = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(tag)
                .when()
                .post(PATH)
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString());

        // create a another tag with the same name
        // BUT associated with a different tag type
        final Tag anotherTag = new Tag();
        anotherTag.name = tagName;
        final TagType anotherTagType = new TagType();
        anotherTagType.id = 19L;
        anotherTag.tagType = anotherTagType;
        anotherTag.id = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(anotherTag)
                .when()
                .post(PATH)
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString());

        // delete both tags to not alter other tests execution
        given()
                .pathParam("id", tag.id)
                .when()
                .delete(PATH + "/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", anotherTag.id)
                .when()
                .delete(PATH + "/{id}")
                .then()
                .statusCode(204);
    }
}
