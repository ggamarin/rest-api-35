package tests;

import io.restassured.response.Response;
import models.UserCreationModel;
import models.UserUpdateModel;
import models.UserDataModel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static specifications.UserSpec.*;

public class ApiTests extends TestBase {
    @Test
    @DisplayName("Проверка списка пользователей")
    void shouldCheckUserListTest() {
        Response response = step("Отправка запроса", () ->
                given(basicUserRequestSpec)
                        .queryParam("page", "2")
                        .get("/users")
                        .then()
                        .statusCode(200)
                        .extract().response()
        );
        step("Проверка ответа", () -> {
            assertThat(response.path("total"), notNullValue());
        });
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void shouldCreateNewUserTest() {
        UserDataModel authData = new UserDataModel();
        authData.setName("batman");
        authData.setJob("vigilante");

        UserCreationModel response = step("Отправка запроса", () ->
                given(basicUserRequestSpec)
                        .body(authData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(userSuccessfulCreationResponseSpec)
                        .extract().as(UserCreationModel.class));
        step("Проверка ответа", () -> {
            assertThat(response.getName(), is(authData.getName()));
            assertThat(response.getJob(), is(authData.getJob()));
        });
    }

    @Test
    @DisplayName("Запрос Patch на изменение пользователя")
    void shouldUpdateUserViaPatchTest() {
        UserDataModel userData = new UserDataModel();
        userData.setName("robin");
        userData.setJob("sidekick");

        UserUpdateModel response = step("Отправка запроса", () ->
                given(basicUserRequestSpec)
                        .body(userData)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(userSuccessfulUpdatingResponseSpec)
                        .extract().as(UserUpdateModel.class));
        step("Проверка ответа", () -> {
            assertThat(response.getName(), is(notNullValue()));
            assertThat(response.getName(), is(userData.getName()));
            assertThat(response.getJob(), is(notNullValue()));
            assertThat(response.getJob(), is(userData.getJob()));
        });
    }

    @Test
    @DisplayName("Запрос PUT на изменение пользователя")
    void shouldUpdateUserViaPutTest() {
        UserDataModel userData = new UserDataModel();
        userData.setName("gordon");
        userData.setJob("comissioner");

        UserUpdateModel response = step("Отправка запроса", () ->
                given(basicUserRequestSpec)
                        .body(userData)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(userSuccessfulUpdatingResponseSpec)
                        .extract().as(UserUpdateModel.class));

        step("Проверка ответа", () -> {
            assertThat(response.getName(), is(notNullValue()));
            assertThat(response.getName(), is(userData.getName()));
            assertThat(response.getJob(), is(notNullValue()));
            assertThat(response.getJob(), is(userData.getJob()));
        });
    }

    @Test
    @DisplayName("Удаление пользователя")
    void checkDeleteUserTest() {
        UserDataModel userData = new UserDataModel();
        userData.setName("batman");
        userData.setJob("vigilante");

        Response response = step("Отправка запроса", () ->
                given(basicUserRequestSpec)
                        .body(userData)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(userSuccessfulDeletingResponseSpec)
                        .extract().response());
        step("Проверка ответа", () -> {
            assertThat(response.asString(), equalTo(""));
        });
    }
}