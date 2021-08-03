package com.perkins.icc.cache;

import com.alibaba.cola.dto.Response;

import java.util.List;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:18
 * @description:
 **/
public interface CacheService {
    public Response execute(RedisCmd cmd);
    public <T> List<T> getQueue(RedisCmd cmd);
}
