package com.perkins.icc.domain.common;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 12:29
 * @description:
 **/
public class Constant {
    //外呼等待队列
    public final static String r_call_queue_key = "queue:waiting_call";
    //空闲坐席队列
    public final static String r_available_agent_queue_key = "queue:available_agent";
}
