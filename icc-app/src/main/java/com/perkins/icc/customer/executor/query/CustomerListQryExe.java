package com.perkins.icc.customer.executor.query;

import com.alibaba.cola.dto.MultiResponse;
import com.perkins.icc.domain.customer.gateway.CustomerGateway;
import com.perkins.icc.dto.CustomerListByNameQry;
import com.perkins.icc.dto.CustomerListQry;
import com.perkins.icc.dto.data.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CustomerListQryExe {
    @Autowired
    private CustomerGateway customerGatewayl;
    public MultiResponse<CustomerDTO> execute(CustomerListQry query) {
        List<CustomerDTO> customerDTOList = customerGatewayl.list(query);
        return MultiResponse.of(customerDTOList);
    }
}
