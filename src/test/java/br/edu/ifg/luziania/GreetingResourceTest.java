package br.edu.ifg.luziania;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testLoginEndpoint() {
        given()
          .when().get("/auth/login")
          .then()
             .statusCode(200)
             .body(containsString("<title>Login</title>"));
    }

}