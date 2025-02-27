package com.redisprac.prac;

import static org.assertj.core.api.Assertions.assertThat;

import com.redisprac.domain.dto.ItemDto;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTemplateTests {
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  /**
   * STRING
   */
  @Test
  public void stringValueOpsTest() {
    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
    ops.set("greeting", "hello redis!");
    assertThat(ops.get("greeting")).isEqualTo("hello redis!");
  }

  /**
   * SET
   */
  @Test
  public void stringSetOpsTest() {
    SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
    setOps.add("hobbies", "games");
    setOps.add("hobbies", "coding");
    setOps.add("hobbies", "alcohol");
    setOps.add("hobbies", "games");
    assertThat(setOps.size("hobbies")).isEqualTo(3);
  }

  /**
   * EXPIRE
   */
  @Test
  public void redisOpsTest() {
    stringRedisTemplate.expire("simplekey", 5, TimeUnit.SECONDS);
    stringRedisTemplate.expire("greeting", 10, TimeUnit.SECONDS);
    stringRedisTemplate.expire("hobbies", 15, TimeUnit.SECONDS);
  }

  @Autowired
  private RedisTemplate<String, ItemDto> itemRedisTemplate;

  @Test
  public void itemRedisTemplateTest() {
    ValueOperations<String, ItemDto> ops = itemRedisTemplate.opsForValue();
    String mechanicalKeyboard = "Mechanical Keyboard";
    int price = 300000;
    ops.set("my:keyboard", ItemDto.builder()
        .name(mechanicalKeyboard)
        .price(price)
        .description("Expensive 😢")
        .build());
    assertThat(ops.get("my:keyboard"))
        .extracting("name", "price")
        .contains(mechanicalKeyboard, price);
  }
}