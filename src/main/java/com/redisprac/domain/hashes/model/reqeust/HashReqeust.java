package com.redisprac.domain.hashes.model.reqeust;

import com.redisprac.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "redis hash collection request")
public record HashReqeust(
    BaseRequest baseRequest,

    @Schema(description = "field")
    @NotBlank
    @NotNull
    String field,

    @Schema(description = "name")
    @NotBlank
    @NotNull
    String name
) {
}  
