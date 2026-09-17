package com.acme.workorder.base.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@TableName("company")
@Data
public class CompanyPO {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private Long id;

    @TableField("company_name")
    @Schema(description = "公司名称")
    private String companyName;

    @TableField("company_code")
    @Schema(description = "公司代码")
    private String companyCode;

    @TableField("company_addr")
    @Schema(description = "公司地址")
    private String companyAddr;

    @TableField("created_by")
    @Schema(description = "创建人")
    private String createdBy;

    @TableField("updated_by")
    @Schema(description = "更新人")
    private String updatedBy;

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createdTime;

    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updatedTime;
}
