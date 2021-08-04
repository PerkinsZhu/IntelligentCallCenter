package com.perkins.icc.domain.cache;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 12:22
 * @description:
 **/
@Builder
@Data
public class RedisCmd<T> {
    private String key;
    private T value;
    private Long expire;
    private Integer limit;
    private boolean isPop;
}
