package ru.netology.ibank;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.ibank.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginWithActiveUser() {
        var authInfo = DataGenerator.getRegisteredActiveUser();
        $("[data-test-id=login] input").setValue(authInfo.getLogin());
        $("[data-test-id=password] input").setValue(authInfo.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(exactText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        var authInfo = DataGenerator.getRegisteredBlockedUser();
        $("[data-test-id=login] input").setValue(authInfo.getLogin());
        $("[data-test-id=password] input").setValue(authInfo.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    void shouldShowErrorWithInvalidLogin() {
        var validUser = DataGenerator.getRegisteredActiveUser();
        $("[data-test-id=login] input").setValue(DataGenerator.generateInvalidLogin());
        $("[data-test-id=password] input").setValue(validUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void shouldShowErrorWithInvalidPassword() {
        var validUser = DataGenerator.getRegisteredActiveUser();
        $("[data-test-id=login] input").setValue(validUser.getLogin());
        $("[data-test-id=password] input").setValue(DataGenerator.generateInvalidPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void shouldShowErrorWithEmptyLogin() {
        $("[data-test-id=password] input").setValue("anypassword");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=login].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorWithEmptyPassword() {
        var validUser = DataGenerator.getRegisteredActiveUser();
        $("[data-test-id=login] input").setValue(validUser.getLogin());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=password].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldOverwriteUserData() {
        var firstUser = DataGenerator.generateUser("active");
        DataGenerator.setUpUser(firstUser);
        var secondUser = new DataGenerator.RegistrationDto(
                firstUser.getLogin(),
                "newpassword" + System.currentTimeMillis(),
                "blocked"
        );
        DataGenerator.setUpUser(secondUser);
        $("[data-test-id=login] input").setValue(secondUser.getLogin());
        $("[data-test-id=password] input").setValue(secondUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Пользователь заблокирован")).shouldBe(visible);
    }
}