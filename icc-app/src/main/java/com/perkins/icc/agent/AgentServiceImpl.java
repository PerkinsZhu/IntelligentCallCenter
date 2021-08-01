package com.perkins.icc.agent;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import com.perkins.icc.api.agent.AgentServiceI;
import com.perkins.icc.dto.CustomerAddCmd;
import org.springframework.stereotype.Service;

/**
 * @author: perkins Zhu
 * @date: 2021/7/31 23:45
 * @description:
 **/
@Service
@CatchAndLog
public class AgentServiceImpl implements AgentServiceI {
    @Override
    public Response addAgent(CustomerAddCmd customerAddCmd) {
        //TODO 在业务库中新增agent信息
        //TODO 在freeswitch中新增agent配置
        return null;
    }
}
