package com.redisprac.domain.redishash.model;

// package, import 생략

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("item")
public class Item implements Serializable {
  @Id // Id가 String으로 쓰면 UUID 자동 배정
  private Long id;
  private String name;
  private String description;
  private Integer price;
}