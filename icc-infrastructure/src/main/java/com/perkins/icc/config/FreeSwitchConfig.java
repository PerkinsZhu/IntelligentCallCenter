package com.perkins.icc.config;

import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
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
        return client;
    }
}
