package com.perkins.icc.web

import com.alibaba.cola.dto.Response
import com.perkins.icc.api.CallServiceI
import com.perkins.icc.dto.CustomerAddCmd
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, ResponseBody}

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/15 17:03
 * @description:
 * */
@Slf4j
@Controller
@RequestMapping(value = Array("/call"))
class CallController(@Autowired callServiceI: CallServiceI) {

  @ResponseBody
  @PostMapping(value = Array("/test"))
  def addCustomer(@RequestBody customerAddCmd: CustomerAddCmd): Response = {
    callServiceI.call(customerAddCmd.getCustomerDTO.getPhone)
    Response.buildSuccess()
  }
}
