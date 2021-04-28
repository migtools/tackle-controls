package io.tackle.controls.util;

import io.restassured.http.ContentType;
import io.tackle.commons.entities.AbstractEntity;

import static io.restassured.RestAssured.given;

public class TestUtils {
    
    public static void testEntityUniqueness(AbstractEntity entity, String resource) {
        // create the entity
        Long firstId = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(entity)
                .when()
                .post(resource)
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString());

        // try to add another time the same entity
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(entity)
                .when()
                .post(resource)
                .then()
                // this will expect a '409' from Quarkus 1.13+ with the introduction of RestDataPanacheException
                .statusCode(500);

        // remove the initial entity
        given()
                .pathParam("id", firstId)
                .when()
                .delete(resource + "/{id}")
                .then()
                .statusCode(204);

        // and check the 'duplicated' entity now will be added
        // proving the partial unique index is working properly with soft-delete
        Long secondId = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(entity)
                .when()
                .post(resource)
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString());

        // remove 'duplicated' entity to not alter other tests
        given()
                .pathParam("id", secondId)
                .when()
                .delete(resource + "/{id}")
                .then()
                .statusCode(204);
    }
}
