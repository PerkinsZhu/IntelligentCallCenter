package com.perkins.icc.domain.call.impl;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.perkins.icc.domain.agents.Agent;
import com.perkins.icc.domain.agents.AgentService;
import com.perkins.icc.domain.call.FsService;
import com.perkins.icc.domain.call.TransferToAgent;
import com.perkins.icc.domain.customer.Customer;
import com.perkins.icc.dto.data.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Response transfer(CustomerDTO customer) {
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
        SingleResponse<String> customerResponse = fsService.callOut(customer.getPhone());
        if (!customerResponse.isSuccess()) {
            log.info("客户未打通，接入重试策略重试");
            //TODO 进入重试策略
            return null;
        }
        String customerUUID = customerResponse.getData();


        SingleResponse<String> agentResponse = fsService.callOut(agent.get().getNo());
        /**
         * TODO 如果坐席未接通，则记录该坐席未接通的数量，可用来考量坐席的工作情况。继续寻找下一个可接通坐席
         * 如果一直 无可用坐席，则给客户播放提前准备好的录音
         */
        if (!agentResponse.isSuccess()) {
            //TODO 播放录音
            return null;
        }
        String agentUUID = agentResponse.getData();


        SingleResponse bridgeResponse = fsService.uuidBridge(customerUUID, agentUUID);
        if(!bridgeResponse.isSuccess()){
            //TODO 记录bridge 失败原因
        }
        //TODO 标记该名单已被接通

        return null;
    }
}
