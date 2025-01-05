package com.redisprac.service;

import com.redisprac.common.redis.RedisCommon;
import com.redisprac.domain.sortedSet.model.SortedSet;
import com.redisprac.domain.sortedSet.model.request.SortedSetRequest;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSortedSet {
    private final RedisCommon redis;

    public void setSortedSet(SortedSetRequest req) {
        SortedSet model = new SortedSet(req.baseRequest().key(), req.name(), req.score());
        redis.addToSortedSet(req.baseRequest().key(), model, req.score());
    }

    public Set<SortedSet> getSetDataByRange(String key, Float min, Float max) {
        return redis.rangeByScore(key, min, max, SortedSet.class);
    }

    public List<SortedSet> getTopN(String key, Integer n) {
        return redis.getTopNFromSortedSet(key, n, SortedSet.class);
    }
}
