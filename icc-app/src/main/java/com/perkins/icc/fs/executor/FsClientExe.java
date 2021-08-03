package com.perkins.icc.fs.executor;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.perkins.icc.dto.data.ErrorCode;
import com.perkins.icc.dto.fs.FsCallCmd;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:22
 * @description:
 **/
@Slf4j
@Service
public class FsClientExe {

    @Autowired
    private Client fsClient;

    public Response simpleCall(FsCallCmd dto) {
        log.info("execute fs cmd:{} {}", dto);
        EslMessage response = fsClient.sendSyncApiCommand(dto.getCommand(), dto.getArgs());
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

    public SingleResponse<String> callOut(FsCallCmd dto) {
        log.info("execute fs cmd:{}", dto);
        EslMessage response = fsClient.sendSyncApiCommand(dto.getCommand(), dto.getArgs());
        log.info("response:{}", response.getBodyLines());
        return getResponse(response);
    }

}
