package com.redisprac.domain.sortedSet.controller;


import com.redisprac.domain.sortedSet.model.SortedSet;
import com.redisprac.domain.sortedSet.model.request.SortedSetRequest;
import com.redisprac.service.RedisSortedSet;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "sorted set ", description = "sorted set api")
@RestController
@RequestMapping("/api/v1/sorted-set")
@RequiredArgsConstructor
public class SortedSetController {
    private final RedisSortedSet redis;

    @PostMapping("/sorted-set-collection")
    public void SetSortedSet(
        @RequestBody @Valid SortedSetRequest req
    ) {
        redis.setSortedSet(req);
    }

    @GetMapping("/get-sorted-set-by-range")
    public Set<SortedSet> getSortedSet(
        @RequestParam @Valid String key,
        @RequestParam @Valid Float min,
        @RequestParam @Valid Float max
    ) {
        return redis.getSetDataByRange(key, min, max);
    }

    @GetMapping("/get-sorted-set-by-top")
    public List<SortedSet> getTopN(
        @RequestParam @Valid String key,
        @RequestParam @Valid Integer n
    ) {
        return redis.getTopN(key, n);
    }

    
}