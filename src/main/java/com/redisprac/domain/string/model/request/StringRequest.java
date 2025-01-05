package com.redisprac.domain.string.model.request;

import com.redisprac.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "redis string collection request")
public record StringRequest(
    BaseRequest baseRequest,

    @Schema(description = "name")
    @NotBlank
    @NotNull
    String name

) {
    
}
