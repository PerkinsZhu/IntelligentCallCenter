package com.perkins.icc.web.agent;

import com.alibaba.cola.dto.Response;
import com.perkins.icc.api.agent.AgentServiceI;
import com.perkins.icc.dto.CustomerAddCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: perkins Zhu
 * @date: 2021/7/31 23:40
 * @description:
 **/
@RestController
public class AgentController {

//    @Autowired
//    private AgentServiceI agentService;

    @PostMapping(value = "/addAgent")
    public Response addAgent(@RequestBody CustomerAddCmd customerAddCmd) {
//        return agentService.addAgent(customerAddCmd);
        return null;
    }
}
