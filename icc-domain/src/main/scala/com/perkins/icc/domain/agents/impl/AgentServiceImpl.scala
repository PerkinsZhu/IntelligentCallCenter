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
    val cmd = RedisCmd[String](Constant.r_available_agent_queue_key, null)
    val list = cacheService.getFromQueue[String](cmd)
    if (list.isEmpty) return Optional.empty
    val agent = Agent(list.get(0), null, null)
    Optional.of(agent)
  }

  override def addReadyAgent(value: String): lang.Boolean = {
    cacheService.addToQueue(RedisCmd(Constant.r_available_agent_queue_key, value))
  }

  override def removeReadyAgent(value: String): lang.Boolean = {
    cacheService.removeFromQueue[String](RedisCmd[String](Constant.r_available_agent_queue_key, value, isPop = true))
  }


  override def getCountByState(status: String): Integer = {
    //TODO 从 redis 中查询出该状态的坐席数量
    10
  }

  override def register(agentStatus: AgentStatus): lang.Boolean = {
    cacheService.addToQueue(RedisCmd(Constant.r_available_agent_queue_key, agentStatus));
    false
  }
}
