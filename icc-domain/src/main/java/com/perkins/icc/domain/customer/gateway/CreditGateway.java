package com.perkins.icc.domain.customer.gateway;

import com.perkins.icc.domain.customer.Customer;
import com.perkins.icc.domain.customer.Credit;

//Assume that the credit info is in antoher distributed Service
public interface CreditGateway {
    public Credit getCredit(String customerId);
}
