package com.perkins.icc.api;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.perkins.icc.dto.CustomerAddCmd;
import com.perkins.icc.dto.CustomerListByNameQry;
import com.perkins.icc.dto.data.CustomerDTO;

public interface CustomerServiceI {

    public Response addCustomer(CustomerAddCmd customerAddCmd);

    public MultiResponse<CustomerDTO> listByName(CustomerListByNameQry customerListByNameQry);
}
