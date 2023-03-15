package com.perkins.icc.call

import com.perkins.icc.api.CallServiceI
import com.perkins.icc.domain.call.TransferToAgent
import com.perkins.icc.dto.data.CustomerDTO
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/15 17:01
 * @description:
 * */

@Slf4j
@Service
class CallServiceImpl(@Autowired transferToAgent: TransferToAgent) extends CallServiceI {


  override def call(phone: String): Boolean = {
    transferToAgent.transfer(CustomerDTO.builder().phone(phone).build())
    true
  }
}
