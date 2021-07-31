package com.perkins.icc.api.agent;

import com.alibaba.cola.dto.Response;
import com.perkins.icc.dto.CustomerAddCmd;

public interface AgentServiceI {
    public Response addAgent(CustomerAddCmd customerAddCmd);
}
