package com.acme.workorder.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCompanyDTO {

    @Schema(description = "主键id")
    @NotNull
    private Long id;

    @Schema(description = "公司名称")
    @NotBlank
    private String companyName;

    @Schema(description = "公司代码")
    @NotBlank
    private String companyCode;

    @Schema(description = "公司地址")
    private String companyAddr;

    @Schema(description = "更新人")
    private String updatedBy;
}
