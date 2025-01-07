package com.redisprac.service;

import com.redisprac.common.redis.RedisCommon;
import com.redisprac.domain.strategy.model.ValueWithTtl;
import com.redisprac.domain.string.model.StringModel;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStrategy {

  final private RedisCommon redis;
  final private RedissonClient redissonClient;

  /**
   * 락을 이용한 순차 보장하는 메서드 메서드
   */
  public void lockSample() {
    RLock lock = redissonClient.getLock("sample");

    try {
      boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);

      if (isLocked) {

      }
    } catch (InterruptedException e) {

    }

    lock.unlock();
  }


  /**
   * 키가 존재를 하면 레디스에서 가져오고 레디스에서 만약 키가 존재하지 않으면 DB에서 가져온 다음에 레디스에 저장을 하는 가장 일반적으로 많이 사용되는 전략
   * 데이터 정합성이 깨질 수 있음
   *
   * @param key
   * @return
   */
  public StringModel simpleStrategy(String key) {
    StringModel model = redis.getData(key, StringModel.class);

    if (model == null) {
      // DB를 조회한 값이라고 가정
      StringModel fromDbData = new StringModel(key, "new db");

      redis.setData(key, fromDbData);

      return fromDbData;
    }

    return model;
  }

  /**
   * 파이프라인
   * @param key
   * @return
   */
  public StringModel perStrategy(String key) {
    ValueWithTtl<StringModel> valueWithTtl = redis.getValueWithTtl(key, StringModel.class);

    if (valueWithTtl != null) {
      // 키가 redis에 있다면 redis에서 가져온다는 점은 동일하지만 비동기로 동작시킨다는 점
      asyncPerStrategy(key, valueWithTtl.getTTL());

      return valueWithTtl.getValue();
    }

    StringModel fromDbData = new StringModel(key, "new db");

    redis.setData(key, fromDbData);

    return fromDbData;
  }


  /**
   * 확률을 계산해서 redis에 업데이트를 하는 알고리즘
   *
   * remain ttl 이 작으면 작을수록 더 잘 발생한다.
   * - 참고: 라인에서 사용하는 알고리즘이다.
   *
   * @param key
   * @param remainTtl
   */
  @Async
  protected void asyncPerStrategy(String key, Long remainTtl) {

    double probability = calculateProbability(remainTtl);

    Random random = new Random();

    // 서버에서 계산한 랜덤한 값보다 크다면 업데이트 해야하는 상황이다.
    if (random.nextDouble() < probability) {
      StringModel fromDB = new StringModel(key, "db from");
      redis.setData(key, fromDB);
    }

  }

  /**
   * 확률측정 - base에 따라 확률이 얼마나 잘 타겟이 되냐 안되냐를 측정
   *
   * 너무 빈번하게 발생한다면 base와 decayRate 조정 필요
   *
   * @param remainTTl
   * @return remainTTL과 base, decayRate를 가지고 확률을 구함
   */
  private double calculateProbability(Long remainTTl) {

    // 기본 확률
    double base = 0.5;
    // 감소율
    double decayRate = 0.1;

    return base * Math.pow(Math.E, -decayRate * remainTTl);
  }

  /**
   * luaScript
   * @param key1
   * @param key2
   * @param newKey
   */
  public void luaScript(String key1, String key2, String newKey) {
    redis.sumTwoKeyAndRenew(key1, key2, newKey);
  }

}
