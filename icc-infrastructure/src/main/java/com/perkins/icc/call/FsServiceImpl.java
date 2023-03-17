package com.perkins.icc.call;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.perkins.icc.domain.call.FsService;
import com.perkins.icc.dto.fs.FsCallCmd;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:06
 * @description:
 **/
@Slf4j
@Service
public class FsServiceImpl implements FsService {

    @Autowired
    private Client fsClient;

    @Override
    public Response simpleCall(FsCallCmd dto) {
        log.info("execute fs cmd:{} {}", dto);
        EslMessage response = fsClient.sendApiCommand(dto.getCommand(), dto.getArgs());
        log.info("response:{}", response.getBodyLines());
        //呼叫成功返回 uuid ,呼叫失败则返回原因
        return getResponse(response);
    }


    private SingleResponse<String> getResponse(EslMessage response) {
        if (response == null || response.getBodyLines().isEmpty()) {
            return SingleResponse.buildFailure("500", "外呼操失败");
        }
        String res = response.getBodyLines().get(0);
        if (res.startsWith("+OK")) {
            return SingleResponse.of(res.substring(3));
        }
        return SingleResponse.buildFailure("500", res);
    }

    @Override
    public SingleResponse<String> callOut(FsCallCmd dto) {
        log.info("execute fs cmd:{}", dto);
        EslMessage response = fsClient.sendApiCommand(dto.getCommand(), dto.getArgs());
        log.info("response:{}", response.getBodyLines());
        return getResponse(response);
    }

    @Override
    public SingleResponse<String> callOut(String phone) {
        FsCallCmd cmd = FsCallCmd.builder()
                .command("originate")
                .args("{origination_caller_id_number=10080001}sofia/gateway/sbc_gw/9" + phone + " &park")
                .build();
        return callOut(cmd);
    }

    @Override
    public SingleResponse<String> callOutAgent(String agentNo) {
        FsCallCmd cmd = FsCallCmd.builder()
                .command("originate")
                .args("user/" + agentNo + " &park")
                .build();
        return callOut(cmd);
    }


    @Override
    public SingleResponse uuidBridge(String a_leg_uuid, String b_leg_uuid) {
        FsCallCmd bridgeCmd = FsCallCmd.builder()
                .command("uuid_bridge")
                .args(a_leg_uuid + " " + b_leg_uuid)
                .build();
        return callOut(bridgeCmd);
    }

}
