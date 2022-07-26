package com.perkins.icc.fs.handler;

import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler;
import org.freeswitch.esl.client.transport.SendMsg;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslHeaders;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;


/**
 * @author: perkins Zhu
 * @date: 2022/7/26 15:08
 * @description:
 **/

@Slf4j
@Component
public class OutboundClientHandler extends AbstractOutboundClientHandler {
    @Override
    protected void handleConnectResponse(ChannelHandlerContext channelHandlerContext, EslEvent event) {
        log.info("Received connect response [{}]", event);
        printHeader(event);

        /**
         * <extension name="outBoundTest">
         *   <condition field="destination_number" expression="76014">
         *     <action application="set" data="taskType=playMusic"/>
         *     <action application="socket" data="serverIp:port async full"/>
         *   </condition>
         * </extension>
         * 这里从事件中取出自定义参数: taskType，然后更加参数类型，进行不同的操作：
         * 转接.挂断.播放音乐/视频
         */
        String taskType = event.getEventHeaders().getOrDefault("variable_taskType", "");
        switch (taskType) {
            case "playMusic":
                log.info("todo 播放音乐");
                break;
            default:
        }
    }

    @Override
    protected void handleEslEvent(ChannelHandlerContext channelHandlerContext, EslEvent eslEvent) {
        log.info("接收到handleEslEvent事件");
    }


    private void hangupCall(Channel channel) {
        SendMsg hangupMsg = new SendMsg();
        hangupMsg.addCallCommand("execute");
        hangupMsg.addExecuteAppName("hangup");
        EslMessage response = this.sendSyncMultiLineCommand(channel, hangupMsg.getMsgLines());
        if (response.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
            log.info("Call hangup successful");
        } else {
            log.error("Call hangup failed: [{}}", response.getHeaderValue(EslHeaders.Name.REPLY_TEXT));
        }
    }

    private void printHeader(EslEvent event) {
        log.info("=======================  incoming channel data  =============================");
        event.getEventHeaders().forEach((k, v) -> {
            log.debug("{} -> {}", k, v);
        });
        log.info("=======================  = = = = = = = = = = =  =============================");
    }
}
