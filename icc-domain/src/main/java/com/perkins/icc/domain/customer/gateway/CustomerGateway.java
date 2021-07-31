package com.perkins.icc.domain.customer.gateway;

import com.perkins.icc.domain.customer.Customer;

public interface CustomerGateway {
    public Customer getByById(String customerId);
}
