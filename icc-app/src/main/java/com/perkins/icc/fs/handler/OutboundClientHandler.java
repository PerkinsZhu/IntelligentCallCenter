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
        if (event.getEventName().equalsIgnoreCase("CHANNEL_DATA")) {
            log.info("=======================  incoming channel data  =============================");
            log.info("Event-Date-Local: [{}]", event.getEventDateLocal());
            log.info("Unique-ID: [{}]", event.getEventHeaders().get("Unique-ID"));
            log.info("Channel-ANI: [{}]", event.getEventHeaders().get("Channel-ANI"));
            log.info("Answer-State: [{}]", event.getEventHeaders().get("Answer-State"));
            log.info("Caller-Destination-Number: [{}]", event.getEventHeaders().get("Caller-Destination-Number"));
            log.info("=======================  = = = = = = = = = = =  =============================");
        } else {
            throw new IllegalStateException("Unexpected event after connect: [" + event.getEventName() + ']');
        }
        //TODO 在这里对 channel进行各种处理
        //转接.挂断.播放音乐/视频

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
}
