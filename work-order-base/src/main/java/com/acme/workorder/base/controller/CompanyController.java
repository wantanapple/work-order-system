package com.acme.workorder.base.controller;

import com.acme.workorder.base.dto.CreatCompanyDTO;
import com.acme.workorder.base.dto.UpdateCompanyDTO;
import com.acme.workorder.base.service.CompanyService;
import com.acme.workorder.base.vo.CompanyPageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;
import com.acme.workorder.common.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公司管理接口。
 */
@Tag(name = "公司管理", description = "提供公司基础信息的增删改查能力")
@RestController
@RequestMapping("/api/work_order_system/base/company")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 新建公司，成功返回新公司主键 id；参数/业务校验异常由 common 模块的 GlobalExceptionHandler 统一处理。
     */
    @Operation(
            summary = "新建公司",
            description = "根据公司名称、公司代码、公司地址创建一条公司记录；名称和代码不能为空。"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "创建成功，返回新公司 id"),
            @ApiResponse(responseCode = "500", description = "创建失败，返回统一错误信息")
    })
    @PostMapping("/create")
    public Result<String> create(
            @Parameter(description = "新建公司请求参数", required = true)
            @Valid @RequestBody CreatCompanyDTO creatCompanyDTO) {
        String id = companyService.create(creatCompanyDTO);
        return Result.ok(id);
    }

    /**
     * 分页查询公司列表，成功返回公司列表；校验失败抛 BizException，由全局异常处理器统一转换。
     */
    @Operation(
            summary = "分页查询公司",
            description = "前端自定义查询条件，进行分页查询"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功，返回分页数据"),
            @ApiResponse(responseCode = "500", description = "查询失败，返回统一错误信息")
    })
    @PostMapping("/page")
    public Result<PageResult<CompanyPageVO>> page(
            @Parameter(description = "分页查询公司请求参数", required = true)
            @Valid @RequestBody PageQuery pageQuery) {
        PageResult<CompanyPageVO> page = companyService.page(pageQuery);
        return Result.ok(page);
    }

    /**
     * 更新公司，成功返回被更新记录的主键 id；参数/业务校验失败抛 BizException，由全局异常处理器统一转换。
     */
    @Operation(
            summary = "更新公司",
            description = "传入需要更新的字段，根据id进行更新"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "更新成功，返回被更新的记录"),
            @ApiResponse(responseCode = "500", description = "更新失败，返回统一错误信息")
    })
    @PostMapping("/update")
    public Result<String> update(
            @Parameter(description = "更新公司请求参数", required = true)
            @Valid @RequestBody UpdateCompanyDTO updateCompanyDTO) {
        String id = companyService.update(updateCompanyDTO);
        return Result.ok(id);
    }

    /**
     * 删除公司，成功返回被删除记录的主键 id 列表；参数/业务校验失败抛 BizException，由全局异常处理器统一转换。
     */
    @Operation(
            summary = "删除公司",
            description = "传入公司主键 id 集合进行批量删除；id 需不重复且均存在，删除成功返回被删除的 id 列表"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功，返回被删除的 id 列表"),
            @ApiResponse(responseCode = "500", description = "删除失败，返回统一错误信息")
    })
    @PostMapping("/delete")
    public Result<List<String>> delete(
            @Parameter(description = "删除公司请求参数", required = true)
            @Valid @RequestBody List<Long> deleteIds) {
        List<String> ids = companyService.delete(deleteIds);
        return Result.ok(ids);
    }
}
