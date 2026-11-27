package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Supplier;
import java.math.BigDecimal;

public record SupplierResponse(
        Long supplierId,
        String name,
        String identityNo,
        String phone,
        String address,
        BigDecimal usableCoffeeKg
) {
    public static SupplierResponse from(Supplier s) {
        return new SupplierResponse(s.getSupplierId(), s.getName(), s.getIdentityNo(),
                s.getPhone(), s.getAddress(), s.getUsableCoffeeKg());
    }
}