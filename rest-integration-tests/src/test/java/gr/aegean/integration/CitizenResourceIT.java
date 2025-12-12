package gr.aegean.integration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CitizenResourceIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8081/rest-1.0-SNAPSHOT/api";
    }

    @Test
    void testGetAll() {
        given()
                .when()
                .get("/citizens")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    void testCreateCitizen() {
        String json = """
                {
                    "firstName": "Carl",
                    "middleName": "Friedrich"
                    "lastName": "Gauss",
                    "dateOfBirth": "30-04-1777",
                    "placeOfBirth": "Brunswick"
                }
                """;

        given()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("/citizens")
                .then()
                .statusCode(201)
                .body("registryNumber", notNullValue())
                .body("firstName", equalTo("John"));
    }

    @Test
    void testGetByRegistry() {
        String id =
                given()
                        .header("Content-Type", "application/json")
                        .body("""
                {"firstName":"Caspar","lastName":"Friedrich"}
                """)
                        .when()
                        .post("/citizens")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("registryNumber");

        given()
                .when()
                .get("/citizens/" + id)
                .then()
                .statusCode(200)
                .body("registryNumber", equalTo(id));
    }

    @Test
    void testDelete() {
        String id =
                given()
                        .header("Content-Type", "application/json")
                        .body("""
                {"firstName":"Bob","lastName":"Dylan"}
                """)
                        .when()
                        .post("/citizens")
                        .then()
                        .extract().path("registryNumber");

        given()
                .when()
                .delete("/citizens/" + id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/citizens/" + id)
                .then()
                .statusCode(404);
    }
}
