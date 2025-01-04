/**
 * redis에서 사용하는 여러가지 모든 값들을 담는다.
 */
package com.redisprac.common.redis;

import com.google.gson.Gson;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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

}

