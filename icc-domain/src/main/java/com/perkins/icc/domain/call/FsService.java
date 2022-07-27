package com.perkins.icc.domain.call;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.perkins.icc.dto.fs.FsCallCmd;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:05
 * @description:
 **/
public interface FsService {
    Response simpleCall(FsCallCmd dto);
    SingleResponse<String> callOut(FsCallCmd dto);

    SingleResponse<String> callOut(String agentNo);

    SingleResponse uuidBridge(String a_leg_uuid, String b_leg_uuid);
}
