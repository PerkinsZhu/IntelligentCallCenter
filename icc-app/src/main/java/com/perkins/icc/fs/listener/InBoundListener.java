package com.perkins.icc.fs.listener;

import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.stereotype.Component;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:13
 * @description:
 **/
@Slf4j
@Component
public class InBoundListener implements IEslEventListener {
    @Override
    public void eventReceived(EslEvent eslEvent) {
        log.info("Event received [{}]", eslEvent);
    }

    @Override
    public void backgroundJobResultReceived(EslEvent eslEvent) {
        log.info("Background job result received [{}]", eslEvent);
    }
}
