package com.perkins.icc.domain.customer.gateway;

import com.perkins.icc.domain.customer.Customer;
import com.perkins.icc.dto.CustomerListQry;
import com.perkins.icc.dto.data.CustomerDTO;

import java.util.List;

public interface CustomerGateway {
    public Customer getByById(String customerId);

    List<CustomerDTO> list(CustomerListQry query);
}
