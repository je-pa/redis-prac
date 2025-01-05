package com.redisprac.domain.string.model.request;

import com.redisprac.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "string multi set request")
public record MultiStringRequest(
    BaseRequest baseRequest,

    @Schema(description = "names")
    @NotBlank
    @NotNull
    String[] names
) {
} 
