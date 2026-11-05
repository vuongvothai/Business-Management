package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Customer;

public record CustomerResponse(Long id, String name, String phone, String address) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getAddress());
    }
}
