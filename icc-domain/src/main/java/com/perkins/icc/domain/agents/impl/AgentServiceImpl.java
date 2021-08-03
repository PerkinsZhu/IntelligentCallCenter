package com.perkins.icc.domain.agents.impl;

import com.perkins.icc.domain.agents.Agent;
import com.perkins.icc.domain.agents.AgentService;
import org.springframework.stereotype.Service;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:44
 * @description:
 **/
@Service
public class AgentServiceImpl implements AgentService {
    @Override
    public Agent findNextAgent() {
        //TODO DRROS
        Agent agent = new Agent();
        agent.setNo("1009");
        return agent;
    }
}
