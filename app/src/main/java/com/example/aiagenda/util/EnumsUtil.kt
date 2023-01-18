package com.example.aiagenda.util

enum class AuthenticationStatus {
    SUCCESS,
    ERROR,
    USER_EXISTS,
    NO_INTERNET_CONNECTION
}

enum class ValidationError() {
    FIRST_NAME_IS_EMPTY,
    LAST_NAME_IS_EMPTY,
    EMAIL_IS_EMPTY,
    EMAIL_NOT_VALID,
    PASSWORD_IS_EMPTY,
    PASSWORD_SHORT,
    YEAR_NOT_SELECTED

}