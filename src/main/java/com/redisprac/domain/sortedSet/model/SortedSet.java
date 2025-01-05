package com.redisprac.domain.sortedSet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SortedSet {
    String key;
    String name;
    Float score;
}
