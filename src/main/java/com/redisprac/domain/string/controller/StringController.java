package com.redisprac.domain.string.controller;

import com.redisprac.domain.string.model.request.MultiStringRequest;
import com.redisprac.domain.string.model.request.StringRequest;
import com.redisprac.domain.string.model.response.StringResponse;
import com.redisprac.service.RedisString;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "string ", description = "string api")
@RestController
@RequestMapping("/api/v1/set")
@RequiredArgsConstructor
public class StringController {
    private final RedisString redis;

    @Operation(
        summary = "set string"
    )
    @PostMapping("/set-string-collection")
    public void SetString(
        @RequestBody @Valid StringRequest req
    ) {
        redis.Set(req);
    }

    @GetMapping("/get-string-collection")
    public StringResponse GetString(
        @RequestParam @Valid String key
    ) {
        return redis.Get(key);
    }

    @PostMapping("/multi-set-collection")
    public void MultiString(
        @RequestBody @Valid MultiStringRequest req
    ) {
        redis.MultiSet(req);
    }
}
