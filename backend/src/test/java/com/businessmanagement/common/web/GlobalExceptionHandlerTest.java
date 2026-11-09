package com.businessmanagement.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsValidationExceptionToUnifiedBadRequest() {
        var target = new Object();
        var errors = new MapBindingResult(new java.util.HashMap<>(), "request");
        errors.addError(new FieldError("request", "weightKg", "must be greater than zero"));
        var exception = new MethodArgumentNotValidException(null, errors);

        var response = handler.handleValidation(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().details()).extracting(ApiError.ApiErrorDetail::field)
                .containsExactly("weightKg");
    }

    @Test
    void mapsStatusExceptionsToExpectedErrorCodes() {
        var unauthorized = handler.handleStatus(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials"));
        var conflict = handler.handleStatus(
                new ResponseStatusException(HttpStatus.CONFLICT, "business conflict"));
        var missing = handler.handleStatus(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "missing"));
        var badRequest = handler.handleStatus(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad request"));

        assertThat(unauthorized.getBody().code()).isEqualTo("UNAUTHORIZED");
        assertThat(conflict.getBody().code()).isEqualTo("BUSINESS_CONFLICT");
        assertThat(missing.getBody().code()).isEqualTo("NOT_FOUND");
        assertThat(badRequest.getBody().code()).isEqualTo("REQUEST_ERROR");
    }

    @Test
    void mapsIllegalArgumentToRequestError() {
        var response = handler.handleIllegalArgument(new IllegalArgumentException("invalid value"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().code()).isEqualTo("REQUEST_ERROR");
        assertThat(response.getBody().message()).isEqualTo("invalid value");
        assertThat(response.getBody().details()).isEmpty();
    }
}
