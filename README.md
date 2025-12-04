# Автотесты для интернет-банка
[![Java CI with Gradle](https://github.com/nazarov23/ibanktest/actions/workflows/build.yml/badge.svg)](https://github.com/nazarov23/ibanktest/actions/workflows/build.yml)

Проект содержит автотесты для функции входа в интернет-банк. Реализована интеграция UI-тестов (Selenide) и API (RestAssured) для создания тестовых пользователей.

## Стек технологий
- Java 11, JUnit 5, Selenide, RestAssured, Gradle
- CI/CD: GitHub Actions

## Структура
- `AuthTest.java` — 7 тестов авторизации
- `DataGenerator.java` — создание пользователей через API
- `build.yml` — workflow для автоматического запуска тестов

## Как запустить
1. Запустить сервер: `java -jar artifacts/app-ibank.jar -P:profile=test`
2. Запустить тесты: `./gradlew test`

Бейдж выше отображает актуальный статус выполнения тестов в CI.