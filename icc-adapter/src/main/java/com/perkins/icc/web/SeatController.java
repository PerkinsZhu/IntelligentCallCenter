package com.perkins.icc.web;

import com.perkins.icc.api.seat.SeatServiceI;
import com.perkins.icc.dto.SeatDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:41
 * @description:
 **/
@Controller
@RequestMapping("/seat")
public class SeatController {
    @Autowired
    SeatServiceI seatService;

    @GetMapping("/call")
    public String call() {
        SeatDto seatDto = new SeatDto();
        seatService.call(seatDto);
        return "success";
    }
}
