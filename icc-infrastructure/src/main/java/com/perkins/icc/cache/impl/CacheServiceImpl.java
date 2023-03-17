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
                .getQueue(getKey(cmd.key()), TypedJsonJacksonCodec.INSTANCE);
        return queue.add(cmd.value());
    }


    @Override
    public <T> List<T> getFromQueue(RedisCmd<T> cmd) {
        RQueue<T> rQueue = redissonClient.getQueue(getKey(cmd.key()), JsonJacksonCodec.INSTANCE);
        return rQueue.poll(cmd.limit());
    }

    @Override
    public <T> Boolean removeFromQueue(RedisCmd<T> cmd) {
        RQueue<T> rQueue = redissonClient.getQueue(getKey(cmd.key()), JsonJacksonCodec.INSTANCE);
        return rQueue.remove(cmd.value());
    }


    @Override
    public <T> T getSortedSet(RedisCmd<T> cmd) {
        RSortedSet<T> sortedSet = redissonClient.getSortedSet(getKey(cmd.key()), JsonJacksonCodec.INSTANCE);
        return sortedSet.first();
    }

    @Override
    public <T> T getValue(RedisCmd<T> cmd) {
        RBucket<T> rBucket = redissonClient.getBucket(getKey(cmd.key()), JsonJacksonCodec.INSTANCE);
        return rBucket.get();
    }

    @Override
    public <T> Boolean setValue(RedisCmd<T> cmd) {
        RBucket<T> rBucket = redissonClient.getBucket(getKey(cmd.key()), JsonJacksonCodec.INSTANCE);
        rBucket.set(cmd.value());
        return Boolean.TRUE;
    }

    private String getKey(String key) {
        return namespace + ":" + key;
    }

}
