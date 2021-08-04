package com.perkins.icc.domain.call.impl;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.perkins.icc.domain.agents.Agent;
import com.perkins.icc.domain.agents.AgentService;
import com.perkins.icc.domain.call.FsService;
import com.perkins.icc.domain.call.TransferToAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 22:55
 * @description:
 **/
@Slf4j
@Service
public class TransferToAgentImpl implements TransferToAgent {


    @Autowired
    FsService fsService;

    @Autowired
    AgentService agentService;

    @Override
    public Response transfer(String a_leg_uuid) {
        //TODO 这里修改为从空闲坐席队列中根据策略获取出转接的坐席
        //TODO 尝试如何使用策略引擎来选取坐席
        /**
         * TODO
         * 坐席十秒内未接则切换为其他坐席
         * 或者坐席也异步呼叫，接通之后一直park着。维护一个空闲坐席队列，然后有客户进来之后，立马转给以接通的空闲坐席。
         * 这样有不用阻塞线程一直等待着坐席接通，
         * 阻塞等待坐席接通会导致每个坐席就耗费一个线程
         */

        Optional<Agent> agent = agentService.findNextAgent();
        if (!agent.isPresent()) {
            log.warn("没有可用空闲坐席");
            //按照呼损处理
            return Response.buildFailure("500", "无可用空闲坐席");
        }
        //如果获取到agent，客户断掉了，此时应该重新把该agent放回等待队列
        SingleResponse rs = fsService.callOut(agent.get().getNo());
        String b_leg_uuid = rs.getData().toString();
        SingleResponse bridge = fsService.uuidBridge(a_leg_uuid, b_leg_uuid);
        return null;
    }
}
