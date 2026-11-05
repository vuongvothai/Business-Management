package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Supplier;
import java.math.BigDecimal;

public record SupplierResponse(
        Long id,
        String name,
        String identityNo,
        String phone,
        String address,
        BigDecimal usableCoffeeKg) {

    public static SupplierResponse from(Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getIdentityNo(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getUsableCoffeeKg());
    }
}
