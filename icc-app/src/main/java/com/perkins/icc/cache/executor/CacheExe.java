package com.perkins.icc.cache.executor;

import com.alibaba.cola.dto.Response;
import com.perkins.icc.dto.cache.RedisCmd;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 12:19
 * @description:
 **/
@Component
public class CacheExe {

    @Value("${spring.redis.namespace:}")
    private String namespace;


    @Autowired
    private RedissonClient redissonClient;

    public Response execute(RedisCmd cmd) {
        boolean success = redissonClient.getQueue(getKey(cmd.getKey()), TypedJsonJacksonCodec.INSTANCE).add(cmd.getValue());
        return success ? Response.buildSuccess() : Response.buildFailure("500", "保存redis队列异常.key:" + cmd.getKey());
    }


    public <T> List<T> getQueue(RedisCmd cmd) {
        RQueue<T> rQueue = redissonClient.getQueue(getKey(cmd.getKey()), JsonJacksonCodec.INSTANCE);
        return rQueue.poll(cmd.getLimit());
    }

    private String getKey(String key) {
        return namespace + ":" + key;
    }

}
