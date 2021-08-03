package com.perkins.icc.dto.fs;

import lombok.Builder;
import lombok.Data;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:24
 * @description:
 **/
@Builder
@Data
public class FsCallCmd {

    private String command;
    private String args;

}
