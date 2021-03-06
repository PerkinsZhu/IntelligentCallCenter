package com.perkins.icc.config;

import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler;
import org.freeswitch.esl.client.outbound.AbstractOutboundPipelineFactory;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:06
 * @description:
 **/
@Slf4j
@Configuration
public class FreeSwitchConfig {
    @Value("${fs.host}")
    private String fsHost;
    @Value("${fs.port}")
    private Integer fsPort;
    @Value("${fs.password}")
    private String fsPassword;

    @Autowired
    IEslEventListener inBoundListener;
    @Autowired
    AbstractOutboundClientHandler outboundClientHandler;

    @Bean
    public Client fsClient() {
        Client client = new Client();
        client.addEventListener(inBoundListener);
//        client.addEventFilter("Caller-Caller-ID-Name","760141");
//        client.addEventFilter("Caller-Caller-ID-Name","10101");


        log.info("Client connecting ..");
        try {
            client.connect(fsHost, fsPort, fsPassword, 2);
        } catch (InboundConnectionFailure e) {
            log.error("Connect failed", e);
            return null;
        }
        log.info("Client connected ..");


//        client.setEventSubscriptions("plain", "all");
        String event = "CHANNEL_ANSWER CHANNEL_APPLICATION CHANNEL_BRIDGE CHANNEL_CALLSTATE CHANNEL_CREATE CHANNEL_DATA CHANNEL_DESTROY CHANNEL_EXECUTE CHANNEL_EXECUTE_COMPLETE CHANNEL_GLOBAL CHANNEL_HANGUP CHANNEL_HANGUP_COMPLETE CHANNEL_HOLD CHANNEL_ORIGINATE CHANNEL_OUTGOING CHANNEL_PARK CHANNEL_PROGRESS CHANNEL_PROGRESS_MEDIA CHANNEL_STATE CHANNEL_UNBRIDGE CHANNEL_UNHOLD CHANNEL_UNPARK CHANNEL_UUID";
        client.setEventSubscriptions("plain", event);
        return client;
    }

    /**
     * ??????????????????
     *
     * <extension name="outBoundTest">
     *   <condition field="destination_number" expression="76014">
     *     <action application="socket" data="serverIp:port async full"/>
     *   </condition>
     * </extension>
     * @return
     */
    @Bean
    public SocketClient socketClient() {

        SocketClient socketClient = null;
        try {
            socketClient = new SocketClient(8022, new AbstractOutboundPipelineFactory(){
                @Override
                protected AbstractOutboundClientHandler makeHandler() {
                    return outboundClientHandler;
                }
            });
            socketClient.start();
        } catch (Exception e) {
            log.error("outBound failed", e);
        }
        log.info("outBound start success");
        return socketClient;
    }
}
