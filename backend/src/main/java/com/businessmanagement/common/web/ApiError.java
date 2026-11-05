package com.businessmanagement.common.web;

import java.util.List;

public record ApiError(String code, String message, List<ApiErrorDetail> details) {

    public record ApiErrorDetail(String field, String message) {
    }
}
