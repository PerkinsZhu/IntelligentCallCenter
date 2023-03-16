package com.perkins.icc.cache.impl;

import com.alibaba.cola.dto.Response;
import com.perkins.icc.domain.cache.CacheService;
import com.perkins.icc.domain.cache.RedisCmd;
import org.redisson.api.RBucket;
import org.redisson.api.RQueue;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:18
 * @description:
 **/
@Service
public class CacheServiceImpl implements CacheService {

    @Value("${spring.redis.namespace:}")
    private String namespace;


    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Boolean addToQueue(RedisCmd cmd) {
        RQueue<Object> queue = redissonClient
                .getQueue(getKey(cmd.getKey()), TypedJsonJacksonCodec.INSTANCE);
        return queue.add(cmd.getValue());
    }


    @Override
    public <T> List<T> getFromQueue(RedisCmd cmd) {
        RQueue<T> rQueue = redissonClient.getQueue(getKey(cmd.getKey()), JsonJacksonCodec.INSTANCE);
        return rQueue.poll(cmd.getLimit());
    }

    @Override
    public <T> Boolean removeFromQueue(RedisCmd cmd) {
        RQueue<T> rQueue = redissonClient.getQueue(getKey(cmd.getKey()), JsonJacksonCodec.INSTANCE);
        return rQueue.remove(cmd.getValue());
    }


    @Override
    public <T> T getSortedSet(RedisCmd cmd) {
        RSortedSet<T> sortedSet = redissonClient.getSortedSet(getKey(cmd.getKey()), JsonJacksonCodec.INSTANCE);
        return sortedSet.first();
    }

    @Override
    public <T> T getValue(RedisCmd<T> cmd) {
        RBucket<T> rBucket = redissonClient.getBucket(getKey(cmd.getKey()), JsonJacksonCodec.INSTANCE);
        return rBucket.get();
    }

    @Override
    public <T> Boolean setValue(RedisCmd<T> cmd) {
        RBucket<T> rBucket = redissonClient.getBucket(getKey(cmd.getKey()), JsonJacksonCodec.INSTANCE);
        rBucket.set(cmd.getValue());
        return Boolean.TRUE;
    }

    private String getKey(String key) {
        return namespace + ":" + key;
    }

}
