package com.acme.workorder.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.acme.workorder.base.dto.CreatCompanyDTO;
import com.acme.workorder.base.dto.UpdateCompanyDTO;
import com.acme.workorder.base.mapper.CompanyMapper;
import com.acme.workorder.base.model.CompanyPO;
import com.acme.workorder.base.service.CompanyService;
import com.acme.workorder.base.vo.CompanyPageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;
import com.acme.workorder.common.enums.ResultCode;
import com.acme.workorder.common.exception.BizException;
import com.acme.workorder.common.util.AssertUtil;
import com.acme.workorder.common.util.QueryWrapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, CompanyPO> implements CompanyService {

    /**
     * 允许作为排序字段的白名单（CompanyPO 属性名），防止前端任意传值拼接进 SQL 造成注入。
     */
    private static final Set<String> ALLOWED_ORDER_FIELDS = Set.of(
            "id", "companyName", "companyCode", "companyAddr",
            "createdBy", "updatedBy", "createdTime", "updatedTime"
    );

    /**
     * 新建公司：参数非空校验 → 公司代码唯一性校验 → 入库，返回新公司主键 id。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreatCompanyDTO creatCompanyDTO) {
        checkCreatCompanyDTO(creatCompanyDTO);
        CompanyPO companyPO = BeanUtil.copyProperties(creatCompanyDTO, CompanyPO.class);
        boolean saved = this.save(companyPO);
        AssertUtil.isTrue(saved, ResultCode.INTERNAL_ERROR, "公司创建失败");
        return String.valueOf(companyPO.getId());
    }

    /**
     * 分页查询公司列表，返回公司列表；pageQuery 为空或排序字段非法时抛 BizException。
     */
    @Override
    public PageResult<CompanyPageVO> page(PageQuery pageQuery) {
        AssertUtil.notNull(pageQuery, ResultCode.BAD_REQUEST, "分页查询参数不能为空");
        //校验前端传过来的分页参数
        pageQuery.normalize();
        //排序字段白名单校验，防止任意值拼入 SQL 造成注入
        validateOrderBy(pageQuery.getOrderBy());
        QueryWrapper<CompanyPO> companyPOQueryWrapper = QueryWrapperUtil.buildWrapper(CompanyPO.class, pageQuery);
        //构建分页对象
        Page<CompanyPO> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<CompanyPO> companyPOPage = this.page(page, companyPOQueryWrapper);
        List<CompanyPageVO> companyPageVOList = BeanUtil.copyToList(companyPOPage.getRecords(), CompanyPageVO.class);
        return PageResult.of(companyPOPage.getTotal(),
                companyPOPage.getCurrent(),
                companyPOPage.getSize(),
                companyPOPage.getPages(),
                companyPOPage.hasNext(),
                companyPOPage.hasPrevious(),
                companyPageVOList);
    }

    /**
     * 更新公司：校验参数与公司代码唯一性 → 按 id 更新，返回被更新记录的主键 id。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(UpdateCompanyDTO updateCompanyDTO) {
        checkUpdateCompanyDTO(updateCompanyDTO);
        CompanyPO companyPO = BeanUtil.copyProperties(updateCompanyDTO, CompanyPO.class);
        boolean updated = this.updateById(companyPO);
        AssertUtil.isTrue(updated, ResultCode.INTERNAL_ERROR, "公司更新失败");
        return String.valueOf(companyPO.getId());
    }

    /**
     * 批量删除公司（物理删除）：入参非空、id 不重复、且全部真实存在时执行删除，返回被删除的主键 id 列表。
     * 校验失败（参数为空 / id 重复 / 包含不存在的 id）直接抛 BizException，由全局异常处理器统一转 Result。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> delete(List<Long> deleteIds) {
        // 1. 入参非空（空集合直接拒绝，避免后续空查询/无意义写库）
        AssertUtil.notEmpty(deleteIds, ResultCode.BAD_REQUEST, "删除参数不能为空");
        // 2. 入参 id 去重：用 HashSet 装一遍，若 size 与入参不一致说明传了重复 id，提前报错
        HashSet<Long> inputSet = new HashSet<>(deleteIds);
        AssertUtil.isEquals(deleteIds.size(), inputSet.size(), ResultCode.BAD_REQUEST, "删除时id不能重复");
        // 3. 查询这些 id 中真实存在的公司
        Set<Long> existIds = this.listByIds(deleteIds).stream().map(CompanyPO::getId).collect(Collectors.toSet());
        AssertUtil.notEmpty(existIds, ResultCode.BIZ_ERROR, "公司不存在");
        // 4. 用「入参集合 removeAll 已存在集合」求差集，差集非空即说明混入了不存在的 id
        inputSet.removeAll(existIds);
        AssertUtil.isTrue(CollectionUtil.isEmpty(inputSet), ResultCode.BAD_REQUEST, "包含不存在的id " + inputSet);
        // 5. 校验通过，批量物理删除；删除成功后返回对应的 id 列表（与入参顺序一致）
        boolean deleted = this.removeByIds(deleteIds);
        AssertUtil.isTrue(deleted, ResultCode.INTERNAL_ERROR, "公司删除失败");
        return deleteIds.stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 更新前校验：
     * 1. 入参非空；
     * 2. 按 id 的公司记录必须存在，否则提示“公司不存在”；
     * 3. 公司代码唯一性——仅当被“其他”记录占用时报错，排除自身（即允许公司代码不变地更新其他字段）。
     */
    private void checkUpdateCompanyDTO(UpdateCompanyDTO updateCompanyDTO) {
        AssertUtil.notNull(updateCompanyDTO, ResultCode.BAD_REQUEST, "参数不能为空");
        // 按 id 查出公司记录，不存在则提示“公司不存在”
        CompanyPO companyPO = this.getById(updateCompanyDTO.getId());
        AssertUtil.notNull(companyPO, ResultCode.BIZ_ERROR, "公司不存在");
        // 公司代码被其他记录占用才报错，排除自身
        boolean occupied = this.exists(new LambdaQueryWrapper<CompanyPO>()
                .eq(CompanyPO::getCompanyCode, updateCompanyDTO.getCompanyCode())
                .ne(CompanyPO::getId, updateCompanyDTO.getId()));
        if (occupied) {
            throw new BizException(ResultCode.BIZ_ERROR, "公司代码已存在");
        }
    }

    /**
     * 校验排序字段必须在白名单内；为空则跳过（由 QueryWrapperUtil 处理）。
     */
    private void validateOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isBlank()) {
            return;
        }
        if (!ALLOWED_ORDER_FIELDS.contains(orderBy)) {
            throw new BizException(ResultCode.BAD_REQUEST, "非法的排序字段: " + orderBy);
        }
    }

    /**
     * 校验参数及公司代码唯一性，失败直接抛 BizException（由全局异常处理器统一转 Result）。
     */
    private void checkCreatCompanyDTO(CreatCompanyDTO creatCompanyDTO) {
        AssertUtil.notNull(creatCompanyDTO, ResultCode.BAD_REQUEST, "参数不能为空");
        //校验公司代码是否已存在
        LambdaQueryWrapper<CompanyPO> wrapper = new LambdaQueryWrapper<CompanyPO>()
                .eq(CompanyPO::getCompanyCode, creatCompanyDTO.getCompanyCode());
        if (this.exists(wrapper)) {
            throw new BizException(ResultCode.BIZ_ERROR, "公司代码已存在");
        }
    }
}
