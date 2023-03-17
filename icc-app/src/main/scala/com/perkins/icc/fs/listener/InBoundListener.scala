package com.perkins.icc.fs.listener

import com.perkins.icc.domain.cache.{CacheService, RedisCmd}
import com.perkins.icc.domain.common.Constant
import com.perkins.icc.domain.event.FsEventType
import com.perkins.icc.fs.handler.EslEventHandler
import lombok.extern.slf4j.Slf4j
import org.freeswitch.esl.client.inbound.IEslEventListener
import org.freeswitch.esl.client.internal.Context
import org.freeswitch.esl.client.transport.event.EslEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/15 15:53
 * @description:
 * */
@Slf4j
@Component
class InBoundListener(@Autowired val cacheService: CacheService, val eslEventHandler: EslEventHandler) extends IEslEventListener {

  val log = LoggerFactory.getLogger(this.getClass)

  override def onEslEvent(context: Context, eslEvent: EslEvent): Unit = {
    //接受阻塞类事件
    eslEventHandler.handle(eslEvent)

  }
}
