package com.perkins.icc.domain.agents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分机信息 FS内容
 *
 * @author: perkins Zhu
 * @date: 2021/8/1 18:31
 * @description:
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Agent {
    //分机号
    private String no;
    private String domain;
    private String host;
}
