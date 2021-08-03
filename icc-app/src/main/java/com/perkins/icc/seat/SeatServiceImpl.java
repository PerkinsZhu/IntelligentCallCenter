package com.perkins.icc.seat;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.perkins.icc.api.seat.SeatServiceI;
import com.perkins.icc.domain.call.FsService;
import com.perkins.icc.dto.SeatDto;
import com.perkins.icc.dto.fs.FsCallCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:44
 * @description:
 **/
@Slf4j
@Service
public class SeatServiceImpl implements SeatServiceI {
    @Autowired
    FsService fsService;

    @Override
    public String call(SeatDto seatDto) {
        callDemo02();
        return null;
    }

    private void callDemo02() {
        //通过bgapi外呼，然后监听坐席接听事件
        FsCallCmd innerCmd = FsCallCmd.builder()
                .command("bgapi originate")
                .args("user/1010 &park")
                .build();
        SingleResponse innerResponse = fsService.callOut(innerCmd);
        log.info("uuid:{}", innerResponse.getData());
    }


    private void cllDemo01() {
        FsCallCmd outCmd = FsCallCmd.builder()
                .command("originate")
//                .args("user/1009 &bridge(sofia/gateway/callcenter_gw/1066)")
//                .args("user/1009 &park")
                .args("sofia/gateway/callcenter_gw/1066 &park")
                .build();
        Response outResponse = fsService.callOut(outCmd);
        log.info("out call result:{}", outResponse.isSuccess());

        //这种方式会导致阻塞，如果 1009 不接听，则线程一直等待中
        FsCallCmd innerCmd = FsCallCmd.builder()
                .command("originate")
                .args("user/1009 &park")
                .build();
        Response innerResponse = fsService.callOut(innerCmd);
        log.info("inner call result:{}", innerResponse.isSuccess());

    }

}
