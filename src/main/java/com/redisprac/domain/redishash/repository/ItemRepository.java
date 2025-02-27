package com.redisprac.domain.redishash.repository;

import com.redisprac.domain.redishash.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {}
