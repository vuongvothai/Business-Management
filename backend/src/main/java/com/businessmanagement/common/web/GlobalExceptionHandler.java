package com.businessmanagement.common.web;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        List<ApiError.ApiErrorDetail> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toDetail)
                .toList();
        return ResponseEntity.badRequest().body(
                new ApiError("VALIDATION_ERROR", "Dữ liệu không hợp lệ", details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException exception) {
        List<ApiError.ApiErrorDetail> details = exception.getConstraintViolations()
                .stream()
                .map(violation -> new ApiError.ApiErrorDetail(
                        violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();
        return ResponseEntity.badRequest().body(
                new ApiError("VALIDATION_ERROR", "Dữ liệu không hợp lệ", details));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleStatus(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());
        String code = status == HttpStatus.UNAUTHORIZED ? "UNAUTHORIZED"
                : status == HttpStatus.CONFLICT ? "BUSINESS_CONFLICT"
                : status == HttpStatus.NOT_FOUND ? "NOT_FOUND" : "REQUEST_ERROR";
        String message = exception.getReason() == null ? "Yêu cầu không hợp lệ" : exception.getReason();
        return ResponseEntity.status(exception.getStatusCode())
                .body(new ApiError(code, message, List.of()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(
                new ApiError("REQUEST_ERROR", exception.getMessage(), List.of()));
    }

    private ApiError.ApiErrorDetail toDetail(FieldError error) {
        return new ApiError.ApiErrorDetail(error.getField(), error.getDefaultMessage());
    }
}
