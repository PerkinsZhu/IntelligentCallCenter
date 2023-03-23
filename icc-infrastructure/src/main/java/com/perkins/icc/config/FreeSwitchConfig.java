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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:06
 * @description:
 **/
@Slf4j
@Configuration
public class FreeSwitchConfig {
    @Value("${fs.remote.host}")
    private String fsHost;
    @Value("${fs.remote.port}")
    private Integer fsPort;
    @Value("${fs.remote.password}")
    private String fsPassword;

    @Value("${fs.local.host}")
    private String localHost;
    @Value("${fs.local.port}")
    private Integer localPort;

    @Autowired
    IEslEventListener inBoundListener;
    @Autowired
    AbstractOutboundClientHandler outboundClientHandler;

    @Bean
    public Client fsClient() {
        Client client = new Client();
        client.addEventListener(inBoundListener);
        log.info("Client connecting ..");
        try {
            client.connect(fsHost, fsPort, fsPassword, 2);
        } catch (InboundConnectionFailure e) {
            log.error("Connect failed", e);
            return null;
        }
        log.info("Client connected ..");

        client.setEventSubscriptions("plain", "all");

        //单独起1个线程，定时检测连接状态
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis() + " " + client.canSend());
            if (!client.canSend()) {
                try {
                    //重连
                    client.connect(fsHost, fsPort, fsPassword, 0);
                    client.cancelEventSubscriptions();
                    client.setEventSubscriptions("plain", "all");
                } catch (Exception e) {
                    log.error("connect fail", e);
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
        return client;
    }

    /**
     * 启动外联服务
     *
     * <extension name="outBoundTest">
     * <condition field="destination_number" expression="76014">
     * <action application="socket" data="serverIp:port async full"/>
     * </condition>
     * </extension>
     *
     * @return
     */
    @Bean
    public SocketClient socketClient() {
        SocketClient socketClient = null;
        try {
            socketClient = new SocketClient(localPort, new AbstractOutboundPipelineFactory() {
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
