package com.perkins.icc.customer.mapper;

import com.perkins.icc.customer.CustomerDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper{

  public CustomerDO getById(String customerId);
}
