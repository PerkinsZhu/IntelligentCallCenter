package com.perkins.icc.domain.agents.impl;

import com.perkins.icc.domain.agents.Agent;
import com.perkins.icc.domain.agents.AgentService;
import com.perkins.icc.domain.cache.CacheService;
import com.perkins.icc.domain.cache.RedisCmd;
import com.perkins.icc.domain.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:44
 * @description:
 **/
@Service
public class AgentServiceImpl implements AgentService {
    @Autowired
    private CacheService cacheService;

    @Override
    public Optional<Agent> findNextAgent() {
        RedisCmd cmd = RedisCmd.builder()
                .key(Constant.r_available_agent_queue_key)
                .limit(1)
                .build();
        List<String> list = cacheService.getQueue(cmd);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        Agent agent = new Agent();
        agent.setNo(list.get(0));
        return Optional.of(agent);
    }
}
