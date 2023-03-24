package com.perkins.icc.fs.handler

import com.perkins.icc.common.EventConstant
import com.perkins.icc.common.EventConstant._
import com.perkins.icc.common.service.BaseDepends
import com.perkins.icc.domain.agents.AgentService
import lombok.extern.log4j.Log4j
import lombok.extern.slf4j.Slf4j
import org.freeswitch.esl.client.transport.event.EslEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/16 10:42
 * @description:
 * */
@Slf4j
@Log4j
@Service
class EslEventHandler(@Autowired agentService: AgentService) extends BaseDepends {


  def handleCustomEvent(implicit eslEvent: EslEvent) = {
    val fromUser = headerValue("from-user")
    val eventSubclass = headerValue("Event-Subclass")
    //    log.info("header:{}", eslEvent.getEventHeaders)

    eventSubclass match {
      case EventConstant.ESL_EVENT_SUBCLASS_NAME_SOFIA_REGISTER => {
        val result = agentService.addReadyAgent(fromUser)
        log.info("添加座席{}-{}", fromUser, result)
      }
      case EventConstant.ESL_EVENT_SUBCLASS_NAME_SOFIA_UNREGISTER => {
        val result = agentService.removeReadyAgent(fromUser)
        log.info("删除座席{}-{}", fromUser, result)
      }
      case _ =>
    }
  }

  def handleHangupComplete(implicit eslEvent: EslEvent) = {
    //TODO 通道关闭，通话结束，可以做一些善后事件
    log.info("通话挂断.{}-->{},HangUpCause:{}",
      headerValue(EVENT_KEY_CALLER_CALLER_ID_NUMBER),
      headerValue(EVENT_KEY_CALLER_DESTINATION_NUMBER),
      headerValue(EVENT_KEY_HANGUP_CAUSE)
    )
  }

  def handle(implicit eslEvent: EslEvent): Unit = {
    val eventName = headerValue("Event-Name")
    val eventSubclass = headerValue("Event-Subclass")
    log.info("inbound receive eventName:{},eventSubclass:{}", eventName, eventSubclass)
    val result = eventName match {
      case EventConstant.ESL_EVENT_NAME_CUSTOM => handleCustomEvent(eslEvent)
      case EventConstant.ESL_EVENT_CHANNEL_HANGUP_COMPLETE => handleHangupComplete(eslEvent)
      case _ => //log.info("receive event_name:{}", eventName)
    }

  }

  def headerValue(key: String)(implicit eslEvent: EslEvent): String = eslEvent.getEventHeaders.getOrDefault(key, "")
}
