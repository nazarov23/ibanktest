package ru.netology.ibank.data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import com.github.javafaker.Faker;
import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    private static final Gson gson = new Gson();
    private static final Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {}

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static void setUpUser(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(user))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static RegistrationDto generateUser(String status) {
        // Генерация логина с помощью Faker
        String login = faker.name().username()
                .toLowerCase()
                .replaceAll("[^a-zа-я0-9._]", "_")
                + faker.number().digits(3);

        // Генерация пароля с помощью Faker
        String password = faker.internet().password(8, 16, true, true, true);

        return new RegistrationDto(login, password, status);
    }

    public static AuthInfo getRegisteredActiveUser() {
        // Используем generateUser вместо дублирования кода
        RegistrationDto user = generateUser("active");
        setUpUser(user);
        return new AuthInfo(user.getLogin(), user.getPassword());
    }

    public static AuthInfo getRegisteredBlockedUser() {
        // Используем generateUser вместо дублирования кода
        RegistrationDto user = generateUser("blocked");
        setUpUser(user);
        return new AuthInfo(user.getLogin(), user.getPassword());
    }

    public static String generateInvalidLogin() {
        // Генерация невалидного логина с помощью Faker
        return "invalid_" + faker.name().username() + "_" + faker.number().digits(6);
    }

    public static String generateInvalidPassword() {
        // Генерация невалидного пароля с помощью Faker
        return "wrong_" + faker.internet().password() + "_" + faker.number().digits(4);
    }
}