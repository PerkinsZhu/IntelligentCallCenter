package com.perkins.icc.agent;

import com.alibaba.cola.dto.SingleResponse;
import com.perkins.icc.domain.agents.Agent;
import com.perkins.icc.dto.fs.FsCallCmd;
import com.perkins.icc.fs.executor.FsClientExe;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: perkins Zhu
 * @date: 2021/8/2 15:14
 * @description:
 **/
@Slf4j
@Service
public class TransferServiceImpl {


    @Autowired
    FsClientExe fsClientExe;

    public boolean bridgeTo(String a_leg_uuid) {
        //TODO 这里修改为从空闲坐席队列中根据策略获取出转接的坐席
        //TODO 尝试如何使用策略引擎来选取坐席
        /**
         * TODO
         * 坐席十秒内未接则切换为其他坐席
         * 或者坐席也异步呼叫，接通之后一直park着。维护一个空闲坐席队列，然后有客户进来之后，立马转给以接通的空闲坐席。
         * 这样有不用阻塞线程一直等待着坐席接通，
         * 阻塞等待坐席接通会导致每个坐席就耗费一个线程
         */
        FsCallCmd cmd = FsCallCmd.builder()
                .command("originate")
                .args("user/1009 &park")
                .build();
        SingleResponse rs = fsClientExe.callOut(cmd);
        String b_leg_uuid = rs.getData().toString();

        FsCallCmd bridgeCmd = FsCallCmd.builder()
                .command("uuid_bridge")
                .args(a_leg_uuid + " " + b_leg_uuid)
                .build();
        SingleResponse bridge = fsClientExe.callOut(bridgeCmd);
        log.info(bridge.toString());
        return true;
    }
}
