package com.springbootinit.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.model.dto.MerchantSearchWrapper;
import com.springbootinit.service.UrUsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 管理员-商户管理接口
 */
@Api(tags = "管理员-商户管理")
@RestController
@RequestMapping("/admin/sjgl")
public class MerchantController {

    @Autowired
    private UrUsersService urUsersService;

    /**
     * 获取商户列表（分页）
     *
     * @param searchWrapper 搜索包装参数
     * @return 商户列表
     */
    @ApiOperation(value = "获取商户列表", notes = "分页获取商户列表，支持按多个字段搜索")
    @PostMapping("/list")
    public BaseResponse<Page<UrUsers>> getMerchants(
            @ApiParam(value = "搜索包装参数") @RequestBody MerchantSearchWrapper searchWrapper) {
        Page<UrUsers> pageInfo = new Page<>(searchWrapper.getPage().getPageNum(), searchWrapper.getPage().getPageSize());
        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", 2); // 商户类型
        if (searchWrapper.getParams().getUsername() != null && !searchWrapper.getParams().getUsername().isEmpty()) {
            queryWrapper.like("username", searchWrapper.getParams().getUsername());
        }
        if (searchWrapper.getParams().getRegionCode() != null && !searchWrapper.getParams().getRegionCode().isEmpty()) {
            queryWrapper.eq("region_code", searchWrapper.getParams().getRegionCode());
        }
        if (searchWrapper.getParams().getStatus() != null) {
            queryWrapper.eq("status", searchWrapper.getParams().getStatus());
        }
        Page<UrUsers> result = urUsersService.page(pageInfo, queryWrapper);
        result.setSize(searchWrapper.getPage().getPageSize());
        result.setTotal(urUsersService.count(queryWrapper));
        result.setCurrent(searchWrapper.getPage().getPageNum());
        return ResultUtils.success(result);
    }

    /**
     * 获取商户详情
     *
     * @param id 商户ID
     * @return 商户详情
     */
    @ApiOperation(value = "获取商户详情", notes = "根据ID获取商户详细信息")
    @PostMapping("/detail")
    public BaseResponse<UrUsers> getMerchant(@ApiParam(value = "商户ID", required = true) @RequestParam Long id) {
        UrUsers merchant = urUsersService.getById(id);
        if (merchant == null || merchant.getUserType() != 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商户不存在");
        }
        return ResultUtils.success(merchant);
    }

    /**
     * 创建商户
     *
     * @param merchant 商户信息
     * @return 创建结果
     */
    @ApiOperation(value = "创建商户", notes = "创建新的商户账号")
    @PostMapping("/create")
    public BaseResponse<Long> createMerchant(@ApiParam(value = "商户信息", required = true) @RequestBody UrUsers merchant) {
        if (merchant.getUsername() == null || merchant.getPasswordHash() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        merchant.setUserType(2);
        merchant.setStatus(1);
        boolean save = urUsersService.save(merchant);
        if (!save) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "创建失败");
        }
        return ResultUtils.success(merchant.getId());
    }

    /**
     * 更新商户
     *
     * @param merchant 商户信息
     * @return 更新结果
     */
    @ApiOperation(value = "更新商户", notes = "更新商户信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateMerchant(@ApiParam(value = "商户信息", required = true) @RequestBody UrUsers merchant) {
        if (merchant.getId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商户ID不能为空");
        }
        UrUsers existing = urUsersService.getById(merchant.getId());
        if (existing == null || existing.getUserType() != 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商户不存在");
        }
        boolean update = urUsersService.updateById(merchant);
        return ResultUtils.success(update);
    }

    /**
     * 删除商户
     *
     * @param id 商户ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除商户", notes = "删除商户账号")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMerchant(@ApiParam(value = "商户ID", required = true) @RequestParam Long id) {
        UrUsers existing = urUsersService.getById(id);
        if (existing == null || existing.getUserType() != 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商户不存在");
        }
        boolean delete = urUsersService.removeById(id);
        return ResultUtils.success(delete);
    }

    /**
     * 导出商户列表到Excel
     */
    @ApiOperation(value = "导出商户列表", notes = "将商户列表导出为Excel文件")
    @PostMapping("/export")
    public void exportMerchants(HttpServletResponse response) throws Exception {
        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", 2);
        List<UrUsers> merchants = urUsersService.list(queryWrapper);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("商户列表", "UTF-8") + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 使用EasyExcel导出
        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), UrUsers.class)
                .sheet("商户列表")
                .doWrite(merchants);
    }

    @ApiOperation(value = "封禁商户", notes = "封禁商户账号")
    @PostMapping("/ban")
    public BaseResponse<Boolean> banMerchant(@ApiParam(value = "商户ID", required = true) @RequestParam Long id) {
        UrUsers existing = urUsersService.getById(id);
        if (existing == null || existing.getUserType() != 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商户不存在");
        }
        UrUsers merchant = new UrUsers();
        merchant.setId(id);
        merchant.setStatus(0);
        boolean update = urUsersService.updateById(merchant);
        return ResultUtils.success(update);
    }

    @ApiOperation(value = "解封商户", notes = "解封商户账号")
    @PostMapping("/unban")
    public BaseResponse<Boolean> unbanMerchant(@ApiParam(value = "商户ID", required = true) @RequestParam Long id) {
        UrUsers existing = urUsersService.getById(id);
        if (existing == null || existing.getUserType() != 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商户不存在");
        }
        UrUsers merchant = new UrUsers();
        merchant.setId(id);
        merchant.setStatus(1);
        boolean update = urUsersService.updateById(merchant);
        return ResultUtils.success(update);
    }

    @ApiOperation(value = "审核通过商户", notes = "审核通过商户账号，将状态从审核中改为正常")
    @PostMapping("/approve")
    public BaseResponse<Boolean> approveMerchant(@ApiParam(value = "商户ID", required = true) @RequestParam Long id) {
        UrUsers existing = urUsersService.getById(id);
        if (existing == null || existing.getUserType() != 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商户不存在");
        }
        if (existing.getStatus() != 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "只有审核中的商户才能审核通过");
        }
        UrUsers merchant = new UrUsers();
        merchant.setId(id);
        merchant.setStatus(1);
        boolean update = urUsersService.updateById(merchant);
        return ResultUtils.success(update);
    }

}
