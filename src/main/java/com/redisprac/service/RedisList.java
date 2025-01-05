package com.redisprac.service;

import com.redisprac.common.redis.RedisCommon;
import com.redisprac.domain.list.model.ListModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisList {
    final private RedisCommon redis;

    public void AddToListLeft(String key, String name) {
        ListModel model = new ListModel(name);
        redis.addToListLeft(key, model);
    }

    public void AddToListRight(String key, String name) {
        ListModel model = new ListModel(name);
        redis.addToListRight(key, model);
    }

    public List<ListModel> GetAllData(String key) {
        return redis.getAllList(key, ListModel.class);
    }
}
