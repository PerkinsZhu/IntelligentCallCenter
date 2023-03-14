package com.perkins.icc.customer

import com.perkins.icc.customer.mapper.CustomerMapper
import com.perkins.icc.domain.customer.Customer
import com.perkins.icc.domain.customer.gateway.CustomerGateway
import com.perkins.icc.dto.CustomerListQry
import com.perkins.icc.dto.data.CustomerDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.{Component, Service}

import scala.jdk.CollectionConverters._
import java.util

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/14 16:46
 * @description:
 * */
@Service
class CustomerGatewayImpl(@Autowired val customerMapper: CustomerMapper) extends CustomerGateway {


  override def getByById(customerId: String): Customer = {
    val customerDO = customerMapper.getById(customerId)
    new Customer()
  }

  override def list(query: CustomerListQry): util.List[CustomerDTO] = {
    customerMapper.list(query)
  }
}
