package com.perkins.icc.seat;

import com.perkins.icc.api.seat.SeatServiceI;
import com.perkins.icc.dto.SeatDto;
import com.perkins.icc.dto.fs.FsCallCmd;
import com.perkins.icc.fs.executor.FsClientExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:44
 * @description:
 **/
@Service
public class SeatServiceImpl implements SeatServiceI {
    @Autowired
    FsClientExe fsClientExe;

    @Override
    public String call(SeatDto seatDto) {
        FsCallCmd cmd = new FsCallCmd();
        fsClientExe.execute(cmd);
        return null;
    }
}
