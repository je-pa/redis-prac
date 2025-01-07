package com.redisprac.domain.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValueWithTtl<T> {
    T Value;
    Long TTL;
}
