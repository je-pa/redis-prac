package com.redisprac.domain.sortedSet.model.request;

import com.redisprac.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SortedSetRequest(
    BaseRequest baseRequest,

    @Schema(description = "name")
    @NotBlank
    @NotNull
    String name,

    @Schema(description = "score")
    @NotBlank
    @NotNull
    Float score
) {
} 
