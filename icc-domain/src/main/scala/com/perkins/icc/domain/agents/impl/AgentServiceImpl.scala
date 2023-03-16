package com.perkins.icc.domain.agents.impl

import com.perkins.icc.domain.agents.{Agent, AgentService, AgentStatus}
import com.perkins.icc.domain.cache.{CacheService, RedisCmd}
import com.perkins.icc.domain.common.Constant
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.{lang, util}
import java.util.Optional

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/16 9:55
 * @description:
 * */
@Slf4j
@Service
class AgentServiceImpl(@Autowired val cacheService: CacheService = null) extends AgentService {

  override def findNextAgent: Optional[Agent] = {
    val cmd = RedisCmd.builder.key(Constant.r_available_agent_queue_key).limit(1).build
    val list = cacheService.getFromQueue(cmd)
    if (list.isEmpty) return Optional.empty
    val agent = new Agent
    agent.setNo(list.get(0))
    Optional.of(agent)
  }

  override def addReadyAgent(value: String): lang.Boolean = {
    cacheService.addToQueue(RedisCmd.builder[String]
      .key(Constant.r_available_agent_queue_key)
      .value(value)
      .build())
  }

  override def removeReadyAgent(value: String): lang.Boolean = {
    cacheService.removeFromQueue(RedisCmd.builder[String]
      .key(Constant.r_available_agent_queue_key)
      .value(value)
      .isPop(true)
      .build())
  }


  override def getCountByState(status: String): Integer = {
    //TODO 从 redis 中查询出该状态的坐席数量
    10
  }

  override def register(agentStatus: AgentStatus): lang.Boolean = {
    cacheService.addToQueue(RedisCmd.builder[AgentStatus]
      .key(Constant.r_available_agent_queue_key)
      .value(agentStatus)
      .build());
    false
  }
}
