package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Customer;

public record CustomerResponse(
        Long customerId,
        String name,
        String phone,
        String address
) {
    public static CustomerResponse from(Customer c) {
        return new CustomerResponse(c.getCustomerId(), c.getName(), c.getPhone(), c.getAddress());
    }
}