package com.perkins.icc.domain.call;

import com.alibaba.cola.dto.Response;
import com.perkins.icc.domain.customer.Customer;
import com.perkins.icc.dto.data.CustomerDTO;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 22:54
 * @description:
 **/
public interface TransferToAgent {
    Response transfer(CustomerDTO customer);
}
