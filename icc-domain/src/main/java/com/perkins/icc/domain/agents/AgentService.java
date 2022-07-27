package com.perkins.icc.domain.agents;

import java.util.Optional;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:44
 * @description:
 **/
public interface AgentService {
    Optional<Agent> findNextAgent();

    Integer getCountByState(String status);
}
