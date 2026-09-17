package com.acme.workorder.base.service;

import com.acme.workorder.base.dto.CreatCompanyDTO;
import com.acme.workorder.base.dto.UpdateCompanyDTO;
import com.acme.workorder.base.model.CompanyPO;
import com.acme.workorder.base.vo.CompanyPageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CompanyService extends IService<CompanyPO> {
    /**
     * 新建公司，成功返回新公司主键 id；校验失败抛 BizException，由全局异常处理器统一转换。
     */
    String create(CreatCompanyDTO creatCompanyDTO);

    /**
     * 分页查询公司列表，成功返回公司列表；校验失败抛 BizException，由全局异常处理器统一转换。
     */
    PageResult<CompanyPageVO> page(PageQuery pageQuery);

    /**
     * 更新公司，成功返回被更新记录的主键 id；参数/业务校验失败抛 BizException，由全局异常处理器统一转换。
     */
    String update(UpdateCompanyDTO updateCompanyDTO);

    /**
     * 批量删除公司（物理删除），成功返回被删除记录的主键 id 列表；
     * 参数为空、id 重复、包含不存在的 id 时抛 BizException，由全局异常处理器统一转换。
     */
    List<String> delete(List<Long> deleteIds);
}
