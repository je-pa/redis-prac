/**
 * redis에서 사용하는 여러가지 모든 값들을 담는다.
 */
package com.redisprac.common.redis;

import com.google.gson.Gson;
import com.redisprac.domain.strategy.model.ValueWithTtl;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
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

    /**
     * list 타입 add left
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void addToListLeft(String key, T value) {
        String jsonValue = gson.toJson(value);
        template.opsForList().leftPush(key, jsonValue);
    }

    /**
     * list 타입 add right
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void addToListRight(String key, T value) {
        String jsonValue = gson.toJson(value);
        template.opsForList().rightPush(key, jsonValue);
    }

    /**
     * list 타입 전체 조회
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> List<T> getAllList(String key, Class<T> clazz) {
        List<String> jsonValues = template.opsForList().range(key, 0, -1);
        List<T> reusltSet = new ArrayList<>();


        if (jsonValues != null) {
            for (String jsonValue : jsonValues) {
                T value = gson.fromJson(jsonValue, clazz);
                reusltSet.add(value);
            }
        }

        return reusltSet;
    }

    /**
     * list 타입 value remove
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void removeFromList(String key, T value) {
        String jsonValue = gson.toJson(value);
        template.opsForList().remove(key, 1, jsonValue);
    }

    /**
     * hash add
     * @param key
     * @param field
     * @param value
     * @param <T>
     */
    public <T> void putInHash(String key, String field, T value) {
        String jsonValue = gson.toJson(value);
        template.opsForHash().put(key, field, jsonValue);
    }

    /**
     * hash field get
     * @param key
     * @param field
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> T getFromHash(String key, String field, Class<T> clazz) {
        Object result  = template.opsForHash().get(key, field);

        if (result != null) {
//            return clazz.cast(result);
            return gson.fromJson(result.toString(), clazz);
        }

        return null;
    }

    /**
     * hash field 삭제
     * @param key
     * @param field
     */
    public void removeFromHash(String key, String field) {
        template.opsForHash().delete(key, field);
    }


    public void setBit(String key, long offset, boolean value) {
        template.opsForValue().setBit(key, offset, value);
    }

    public boolean getBit(String key, long offset) {
        return template.opsForValue().getBit(key, offset);
    }

    /**
     * Pipeline - Atomic 보장 안됨
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> ValueWithTtl<T> getValueWithTtl(String key, Class<T> clazz) {
        T value = null;
        Long ttl = null;

        try {
            List<Object> results = template.executePipelined(new RedisCallback<Object>() {
                // 해당 내부에서 파이프라인을 작성
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisConnection conn = (StringRedisConnection) connection;
                    conn.get(key);
                    conn.ttl(key);

                    return null;
                }
            });

            value = (T) gson.fromJson((String) results.get(0), clazz);
            ttl = (Long) results.get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ValueWithTtl<T>(value, ttl);
    }

    /**
     * LuaScript - Atomic 보장
     * @param key1
     * @param key2
     * @param resultKey
     * @return
     */
    public Long sumTwoKeyAndRenew(String key1, String key2, String resultKey) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();

        redisScript.setLocation(new ClassPathResource("/lua/newKey.lua"));
        redisScript.setResultType(Long.class);

        List<String> keys = Arrays.asList(key1, key2, resultKey);

        return template.execute(redisScript, keys);
    }

    public Long sumTwoKeyAndRenew(String script, String key1, String key2, String resultKey) {
        return template.execute((RedisCallback<Long>) connection -> {
           byte[] scriptBytes = script.getBytes();
           byte[] key1Bytes = key1.getBytes();
           byte[] key2Bytes = key2.getBytes();
           byte[] resultKeyBytes = resultKey.getBytes();

           return (Long) connection.execute("EVAL",
               scriptBytes,
               key1Bytes,
               key2Bytes,
               resultKeyBytes
               );
        });
    }
}

