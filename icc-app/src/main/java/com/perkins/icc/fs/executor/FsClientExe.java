package com.perkins.icc.fs.executor;

import com.alibaba.cola.dto.Response;
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

    public Response execute(FsCallCmd dto) {
        log.info("execute fs cmd:{}", dto);
        //The flow of usecase is defined here.
        //The core ablility should be implemented in Domain. or sink to Domian gradually
        int code = 1;
        EslMessage response = fsClient.sendSyncApiCommand("originate", "user/1009 &bridge(sofia/gateway/callcenter_gw/1066)");
        log.info("response:{}", response.getBodyLines());
        if (code != 1) {
            throw new BizException(ErrorCode.B_CUSTOMER_companyNameConflict.getErrCode(), "fs 执行失败");
        }
        return Response.buildSuccess();
    }
}
