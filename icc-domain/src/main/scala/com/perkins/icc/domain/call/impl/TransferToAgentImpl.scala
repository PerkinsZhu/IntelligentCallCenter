package com.perkins.icc.domain.call.impl

import com.alibaba.cola.dto.{Response, SingleResponse}
import com.perkins.icc.domain.agents.{Agent, AgentService}
import com.perkins.icc.domain.call.{FsService, TransferToAgent}
import com.perkins.icc.dto.data.CustomerDTO
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.Optional

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/15 17:49
 * @description:
 * */
@Slf4j
@Service
class TransferToAgentImpl(@Autowired fsService: FsService, @Autowired agentService: AgentService) extends TransferToAgent {
  val log = LoggerFactory.getLogger(this.getClass)

  override def transfer(customer: CustomerDTO): Response = {
    //TODO 这里修改为从空闲坐席队列中根据策略获取出转接的坐席
    //TODO 尝试如何使用策略引擎来选取坐席
    //TODO 是先打座席还是先打客户？如何避免座席被其他任务占用？
    /**
     * TODO
     * 坐席十秒内未接则切换为其他坐席
     * 或者坐席也异步呼叫，接通之后一直park着。维护一个空闲坐席队列，然后有客户进来之后，立马转给以接通的空闲坐席。
     * 这样有不用阻塞线程一直等待着坐席接通，
     * 阻塞等待坐席接通会导致每个坐席就耗费一个线程
     */
    //获取空闲座席
    val agent = agentService.findNextAgent();
    if (!agent.isPresent) {
      log.warn("没有可用空闲坐席")
      //按照呼损处理
      return Response.buildFailure("500", "无可用空闲坐席")
    }
    //如果获取到agent，客户断掉了，此时应该重新把该agent放回等待队列
    val customerResponse: SingleResponse[String] = fsService.callOut(customer.getPhone)
    if (!customerResponse.isSuccess) {
      log.info("客户未打通，接入重试策略重试")
      //TODO 进入重试策略
      return null
    }

    val customerUUID: String = customerResponse.getData
    log.info("customerUUID:{}", customerUUID)

    val agentResponse: SingleResponse[String] = fsService.callOutAgent(agent.get.getNo)

    /**
     * TODO 如果坐席未接通，则记录该坐席未接通的数量，可用来考量坐席的工作情况。继续寻找下一个可接通坐席
     * 如果一直 无可用坐席，则给客户播放提前准备好的录音
     */
    if (!agentResponse.isSuccess) { //TODO 播放录音
      return null
    }
    val agentUUID: String = agentResponse.getData
    log.info("agentUUID:{}", agentUUID)

    val bridgeResponse: SingleResponse[_] = fsService.uuidBridge(customerUUID, agentUUID)
    if (!bridgeResponse.isSuccess) {
      log.warn("bridge failed.response:{}", bridgeResponse)
    }

    //TODO 标记该名单已被接通

    return null
  }
}
