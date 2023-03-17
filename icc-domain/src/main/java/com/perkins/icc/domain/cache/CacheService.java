package com.perkins.icc.domain.cache;

import com.alibaba.cola.dto.Response;
import com.perkins.icc.domain.cache.RedisCmd;

import java.util.List;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 23:18
 * @description:
 **/
public interface CacheService {
    public <T> Boolean addToQueue(RedisCmd<T> cmd);

    public <T> List<T> getFromQueue(RedisCmd<T> cmd);

    public <T> Boolean removeFromQueue(RedisCmd<T> cmd);

    public <T> T getSortedSet(RedisCmd<T> cmd);

    public <T> T getValue(RedisCmd<T> cmd);

    public <T> Boolean setValue(RedisCmd<T> cmd);
}
