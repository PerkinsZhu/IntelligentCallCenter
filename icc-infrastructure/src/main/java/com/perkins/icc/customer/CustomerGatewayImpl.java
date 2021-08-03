package com.perkins.icc.customer;

import com.perkins.icc.customer.mapper.CustomerMapper;
import com.perkins.icc.domain.customer.Customer;
import com.perkins.icc.domain.customer.gateway.CustomerGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerGatewayImpl implements CustomerGateway {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Customer getByById(String customerId){
      CustomerDO customerDO = customerMapper.getById(customerId);
      //Convert to Customer
      return null;
    }
}
