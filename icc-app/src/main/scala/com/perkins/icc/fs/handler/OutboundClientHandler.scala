package com.perkins.icc.fs.handler

import lombok.extern.slf4j.Slf4j
import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler
import org.freeswitch.esl.client.transport.SendMsg
import org.freeswitch.esl.client.transport.event.EslEvent
import org.freeswitch.esl.client.transport.message.{EslHeaders, EslMessage}
import org.jboss.netty.channel.{Channel, ChannelHandlerContext, ExceptionEvent}
import org.springframework.stereotype.Component
import scala.jdk.CollectionConverters._


/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/15 10:37
 * @description:
 * */
@Slf4j
@Component
class OutboundClientHandler extends AbstractOutboundClientHandler {


  override def handleConnectResponse(channelHandlerContext: ChannelHandlerContext, eslEvent: EslEvent): Unit = {
    log.info("Received connect response [{}]", eslEvent)
//    printHeader(eslEvent)
    /**
     * 见 https://www.cnblogs.com/yjmyzz/p/freeswitch-esl-java-client-turorial.html
     *
     * <extension name="outBoundTest">
     * <condition field="destination_number" expression="76014">
     * <action application="set" data="taskType=playMusic"/>
     * <action application="socket" data="serverIp:port async full"/>
     * </condition>
     * </extension>
     * 这里从事件中取出自定义参数: taskType，然后更加参数类型，进行不同的操作：
     * 转接.挂断.播放音乐/视频
     */
    val taskType = eslEvent.getEventHeaders.getOrDefault("variable_taskType", "")

    implicit val channel = channelHandlerContext.getChannel
    implicit val uuid = eslEvent.getMessageHeaders.getOrDefault("Core-UUID", "")

    taskType match {
      case "playMusic" =>
      //转座席
      //        send("execute", "bridge", "user/1008")
      //        send("execute", "playback", "/home/zpj/wav/123.wav")
      // 挂断电话
      //        send("execute", "hangup", null)
      case _ =>
    }

  }

  override def handleEslEvent(channelHandlerContext: ChannelHandlerContext, eslEvent: EslEvent): Unit = {
    log.info("接收到handleEslEvent事件")
  }


  def send(command: String, appName: String, arg: String)(implicit channel: Channel, uuid: String): Unit = {
    val sendMsg = new SendMsg(uuid)
    sendMsg.addCallCommand(command)
    sendMsg.addExecuteAppName(appName)
    sendMsg.addExecuteAppArg(arg)
    sendMsg.addEventLock()

    val response = this.sendSyncMultiLineCommand(channel, sendMsg.getMsgLines)
//    response.getHeaders.forEach((k, v) => log.info("{}-->{}", k, v))
    if (response.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
      log.info("sendMsg {} successful", appName)
    } else {
      log.error("sendMsg {} failed: {}", appName, response.getBodyLines.asScala)
    }
  }

  private def printHeader(event: EslEvent): Unit = {
    log.info("=======================  incoming channel data  =============================")
    event.getEventHeaders.forEach((k, v) => log.debug("{} -> {}", k, v))
    log.info("=======================  = = = = = = = = = = =  =============================")
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) {
    log.error("exception caught:{}", e)
  }
}
