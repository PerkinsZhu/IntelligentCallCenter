package com.perkins.icc.fs.handler

import com.perkins.icc.common.service.BaseDepends
import lombok.extern.slf4j.Slf4j
import org.freeswitch.esl.client.dptools.{Execute, ExecuteException}
import org.freeswitch.esl.client.internal.Context
import org.freeswitch.esl.client.outbound.IClientHandler
import org.freeswitch.esl.client.transport.{CommandResponse, SendMsg}
import org.freeswitch.esl.client.transport.event.EslEvent
import org.freeswitch.esl.client.transport.message.EslHeaders
import org.springframework.stereotype.Component


/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/15 10:37
 * @description:
 * */
@Slf4j
@Component
class OutboundClientHandler extends BaseDepends with IClientHandler {

  def doAction(implicit context: Context, uuid: String): Unit = {
    val exe = new Execute(context, uuid)
    try {

      log.info("===============answer=============")
      //      exe.answer()
      sendMsg("bridge", "user/1008")
      log.info("===============answer end =============")
      //      exe.hangup()

      //      exe.bridge("user/1008")
      //      exe.playback("/home/zpj/wav/123.wav")
      //      exe.playbackVolume(10)
      //      exe.displaceSession("/usr/local/freeswitch/sounds/music/8000/danza-espanola-op-37-h-142-xii-arabesca.wav")
    } catch {
      case exception: Exception => log.error("============answer error ====", exception)
    } finally {
      log.info("===========answer finally=====")
      exe.hangup("zpj")
    }

  }

  def sendMsg(app: String, args: String)(implicit context: Context): Unit = {
    val msg = new SendMsg
    msg.addCallCommand("execute")
    msg.addExecuteAppName(app)
    msg.addExecuteAppArg(args)
    val resp = context.sendMessage(msg)
    if (!resp.isOk) {
      log.error("send msg error:{}", resp.getReplyText)
    }
  }

  override def onConnect(context: Context, eslEvent: EslEvent): Unit = {
    log.info("Received connect response [{}]", eslEvent)
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


    implicit val coreUUid = eslEvent.getMessageHeaders.getOrDefault("Core-UUID", "")
    implicit val uuid = eslEvent.getMessageHeaders.getOrDefault("unique-id", "")

    taskType match {
      case "playMusic" =>
        //转座席
        //        send("execute", "bridge", "user/1008")
        //        send("execute", "playback", "/home/zpj/wav/123.wav")
        // 挂断电话
        //        send("execute", "hangup", null)
        doAction(context, uuid)

      case _ =>
    }


  }

  override def onEslEvent(context: Context, eslEvent: EslEvent): Unit = {
    log.info("接收到handleEslEvent事件")
  }


  private def printHeader(event: EslEvent): Unit = {
    log.info("=======================  incoming channel data  =============================")
    event.getEventHeaders.forEach((k, v) => log.debug("{} -> {}", k, v))
    log.info("=======================  = = = = = = = = = = =  =============================")
  }
}
