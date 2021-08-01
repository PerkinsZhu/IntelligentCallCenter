package com.perkins.icc.domain.agents;

import lombok.Data;

/** 分机信息 FS内容
 * @author: perkins Zhu
 * @date: 2021/8/1 18:31
 * @description:
 **/
@Data
public class Agent {
    //分机号
    private String no;
    private String domain;
    private String host;
}
