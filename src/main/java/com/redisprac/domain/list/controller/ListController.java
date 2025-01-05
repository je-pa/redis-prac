package com.redisprac.domain.list.controller;

import com.redisprac.domain.list.model.ListModel;
import com.redisprac.domain.list.model.request.ListReqeust;
import com.redisprac.service.RedisList;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "list", description = "list api")
@RestController
@RequestMapping("/api/v1/list")
@RequiredArgsConstructor
public class ListController {
    private final RedisList redis;

    @PostMapping("/list-add-left")
    public void SetNewValueToList(
        @RequestBody @Valid ListReqeust req
    ) {
        redis.AddToListLeft(req.baseRequest().key(), req.Name());
    }

    @PostMapping("/list-add-right")
    public void SetNewValueToRight(
        @RequestBody @Valid ListReqeust req
    ) {
        redis.AddToListRight(req.baseRequest().key(), req.Name());
    }

    @GetMapping("/all")
    public List<ListModel> GetAll(
        @RequestParam String key
    ) {
        return redis.GetAllData(key);
    }
}
