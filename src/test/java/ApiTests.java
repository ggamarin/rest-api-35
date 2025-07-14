import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ApiTests {
    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in/";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Проверка списка пользователей")
    void shouldCheckUserListTest() {
        given()
                .log().uri()
                .queryParam("page", "2")
                .get("/users")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("total", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Создание нового юзера")
    void shouldCreateNewUserTest() {
        String newUser = "{\"name\": \"batman\", \"job\": \"vigilante\"}";
        given()
                .body(newUser)
                .header("x-api-key", "reqres-free-v1")
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("id", is(Matchers.notNullValue()))
                .body("name", is("batman"))
                .body("job", is("vigilante"));
    }

    @Test
    @DisplayName("Запрос Patch на измененин пользователя")
    void shouldUpdateUserViaPatchTest() {
        String updateUser = "{\"name\": \"robin\", \"job\": \"sidekick\"}";
        given()
                .header("x-api-key", "reqres-free-v1")
                .body(updateUser)
                .contentType(JSON)
                .log().uri()
                .when()
                .patch("/api/users/426")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("name", equalTo("robin"))
                .body("job", equalTo("sidekick"));
    }

    @Test
    @DisplayName("Запрос PUT на изменеие юзера")
    void shouldUpdateUserViaPutTest() {
        String updateUserPut = "{\"name\": \"gordon\", \"job\": \"comissioner\"}";
        given()
                .header("x-api-key", "reqres-free-v1")
                .body(updateUserPut)
                .contentType(JSON)
                .log().uri()
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("gordon"))
                .body("job", is("comissioner"));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void checkDeleteUserTest() {
        given()
                .header("x-api-key", "reqres-free-v1")
                .log().uri()
                .when()
                .delete("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}