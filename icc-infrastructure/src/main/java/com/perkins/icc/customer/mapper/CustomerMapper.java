package com.perkins.icc.customer.mapper;

import com.perkins.icc.customer.CustomerDO;
import com.perkins.icc.dto.CustomerListQry;
import com.perkins.icc.dto.data.CustomerDTO;

import java.util.List;

public interface CustomerMapper {

    CustomerDO getById(String customerId);

//    @Select("select * from customer limit 10;")
    List<CustomerDTO> list(CustomerListQry query);
}
