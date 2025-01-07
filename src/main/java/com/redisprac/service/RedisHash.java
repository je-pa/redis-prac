package com.redisprac.service;

import com.redisprac.common.redis.RedisCommon;
import com.redisprac.domain.hashes.model.HashModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisHash {
    final private RedisCommon redis;

    public void putInHash(String key, String field, String name) {
        HashModel model = new HashModel(name);
        redis.putInHash(key, field, model);
    }

    public HashModel GetFromHash(String key, String field) {
        return redis.getFromHash(key, field, HashModel.class);
    }
}
