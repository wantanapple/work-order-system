package com.acme.workorder.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatCompanyDTO {

    @Schema(description = "公司名称")
    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    @Schema(description = "公司代码")
    @NotBlank(message = "公司代码不能为空")
    private String companyCode;

    @Schema(description = "公司地址")
    private String companyAddr;

    @Schema(description = "创建人")
    private String createdBy;
}
