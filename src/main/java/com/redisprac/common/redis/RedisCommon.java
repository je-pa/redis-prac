/**
 * redis에서 사용하는 여러가지 모든 값들을 담는다.
 */
package com.redisprac.common.redis;

import com.google.gson.Gson;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisCommon {
    private final RedisTemplate<String, String> template;

    // 직렬화하는 패키지
    private final Gson gson;

    @Value("${spring.redis.default-time}")
    private Duration defaultExpireTime;

    /**
     * String 데이터 조회
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> T getData(String key, Class<T> clazz) {
        String jsonValue = template.opsForValue().get(key);
        if (jsonValue == null) { 
            return null;
        } 

        return gson.fromJson(jsonValue, clazz);
    }

    /**
     * String 데이터 저장
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void setData(String key, T value) {
        // 직렬화
        String jsonValue = gson.toJson(value);
        template.opsForValue().set(key, jsonValue);
        template.expire(key, defaultExpireTime);
    }

    /**
     * multi set
     * @param datas
     * @param <T>
     */
    public <T> void multiSetData(Map<String, T> datas) {
        Map<String, String> jsonMap = new HashMap<>();

        for (Map.Entry<String, T> entry : datas.entrySet()) {
            jsonMap.put(entry.getKey(), gson.toJson(entry.getValue()));
        }

        template.opsForValue().multiSet(jsonMap);
    }


    /**
     * 정렬되어 있는 set
     * @param key
     * @param value
     * @param score 정렬의 기준이 될 값
     * @param <T>
     */
    public <T> void addToSortedSet(String key, T value, Float score) {
        // 직렬화
        String jsonValue = gson.toJson(value);
        template.opsForZSet().add(key, jsonValue, score);
    }

    /**
     * sorted set 범위 조회
     * @param key
     * @param minScore
     * @param maxScore
     * @param clazz 어떤 클래스로 역직렬화 할지
     * @return minScore ~ maxScore 사이에 있는 데이터들
     * @param <T>
     */
    public <T> Set<T> rangeByScore(String key, Float minScore, Float maxScore, Class<T> clazz) {
        Set<String> jsonValues = template.opsForZSet().rangeByScore(key, minScore, maxScore);
        Set<T> resultSet = new HashSet<T>();

        if (jsonValues != null) {
            // 역직렬화
            for (String jsonValue : jsonValues) {
                T v = gson.fromJson(jsonValue, clazz);
                resultSet.add(v);
            }
        }

        return resultSet;
    }

    /**
     * sorted set 랭킹 조회
     * @param key
     * @param n
     * @param clazz
     * @return 상위 데이터들 조회
     * @param <T>
     */
    public <T> List<T> getTopNFromSortedSet(String key, int n, Class<T> clazz) {
        Set<String> jsonValues = template.opsForZSet().reverseRange(key, 0, n-1);
        List<T> resultSet = new ArrayList<T>();

        if (jsonValues != null) {
            for (String jsonValue : jsonValues) {
                T v = gson.fromJson(jsonValue, clazz);
                resultSet.add(v);
            }
        }

        return resultSet;
    }



}

