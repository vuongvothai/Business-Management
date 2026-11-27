package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Warehouse;

public record WarehouseResponse(Long warehouseId, String code, String name, String address, boolean active) {
    public static WarehouseResponse from(Warehouse w) {
        return new WarehouseResponse(w.getWarehouseId(), w.getCode(), w.getName(), w.getAddress(), w.isActive());
    }
}