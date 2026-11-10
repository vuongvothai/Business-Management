package com.businessmanagement.market.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketPriceRequest(
        @DecimalMin(value = "0.00", inclusive = false, message = "Phải lớn hơn 0")
        BigDecimal pricePerKg,
        @NotBlank(message = "Bắt buộc nhập nguồn") @Size(max = 255) String source,
        LocalDateTime fetchedAt) {
}
