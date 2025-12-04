package ru.netology.ibank.data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
        .setBaseUri("http://localhost")
        .setPort(9999)
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();

    private static final Gson gson = new Gson();
    private static final Random random = new Random();

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
        String login = "user" + System.currentTimeMillis() + random.nextInt(1000);
        String password = "pass" + System.currentTimeMillis();
        return new RegistrationDto(login, password, status);
    }

    public static AuthInfo getRegisteredActiveUser() {
        String login = "user" + System.currentTimeMillis() + random.nextInt(1000);
        String password = "pass" + System.currentTimeMillis();
        RegistrationDto user = new RegistrationDto(login, password, "active");
        setUpUser(user);
        return new AuthInfo(login, password);
    }

    public static AuthInfo getRegisteredBlockedUser() {
        String login = "blocked" + System.currentTimeMillis() + random.nextInt(1000);
        String password = "pass" + System.currentTimeMillis();
        RegistrationDto user = new RegistrationDto(login, password, "blocked");
        setUpUser(user);
        return new AuthInfo(login, password);
    }

    public static String generateInvalidLogin() {
        return "invalid" + System.currentTimeMillis();
    }

    public static String generateInvalidPassword() {
        return "wrong" + System.currentTimeMillis();
    }
}