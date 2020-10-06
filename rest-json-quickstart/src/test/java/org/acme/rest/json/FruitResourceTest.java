package org.acme.rest.json;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FruitResourceTest {

    @Test
    public void testList() {
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", is(3),
                        "name", containsInAnyOrder("Apple", "Pineapple", "Unknown"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit", "Unknown fruit"),
                        "family", containsInAnyOrder("simple", "complex", "4test")
                        );
    }

    @Test
    public void testAdd() {
        given()
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\", \"family\": \"simple\" , \"families\": [ \"simple\" ]  }" )
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", is(4),
                        "name", containsInAnyOrder("Apple", "Pineapple", "Pear", "Unknown"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit", "Winter fruit", "Unknown fruit"),
                        "family", containsInAnyOrder("simple", "complex", "4test", "simple")
                        );

        given()
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", is(3),
                        "name", containsInAnyOrder("Apple", "Pineapple", "Unknown"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit", "Unknown fruit"),
                        "family", containsInAnyOrder("simple", "complex", "4test")
                        );
    }
}
