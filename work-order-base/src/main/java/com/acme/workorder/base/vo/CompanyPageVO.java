package com.acme.workorder.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 分页查询返回前端的数据
 */
@Data
public class CompanyPageVO {

    @Schema(description = "主键id")
    private String id;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司代码")
    private String companyCode;

    @Schema(description = "公司地址")
    private String companyAddr;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "更新人")
    private String updatedBy;

    @Schema(description = "创建时间")
    private Date createdTime;

    @Schema(description = "更新时间")
    private Date updatedTime;
}
