package com.springbootinit.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.model.dto.AdminSearchWrapper;
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
 * 管理员-管理员管理接口
 */
@Api(tags = "管理员-管理员管理")
@RestController
@RequestMapping("/admin/glygl")
public class AdminManagementController {

    @Autowired
    private UrUsersService urUsersService;

    /**
     * 获取管理员列表（分页）
     *
     * @param searchWrapper 搜索包装参数
     * @return 管理员列表
     */
    @ApiOperation(value = "获取管理员列表", notes = "分页获取管理员列表，支持按多个字段搜索")
    @PostMapping("/list")
    public BaseResponse<Page<UrUsers>> getAdmins(
            @ApiParam(value = "搜索包装参数") @RequestBody AdminSearchWrapper searchWrapper) {
        Page<UrUsers> pageInfo = new Page<>(searchWrapper.getPage().getPageNum(), searchWrapper.getPage().getPageSize());
        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", 3); // 管理员类型
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
        return ResultUtils.success(result);
    }

    /**
     * 获取管理员详情
     *
     * @param userId 管理员ID
     * @return 管理员详情
     */
    @ApiOperation(value = "获取管理员详情", notes = "根据ID获取管理员详细信息")
    @PostMapping("/detail")
    public BaseResponse<UrUsers> getAdmin(@ApiParam(value = "管理员ID", required = true) @RequestParam Long userId) {
        UrUsers admin = urUsersService.getById(userId);
        if (admin == null || admin.getUserType() != 3) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "管理员不存在");
        }
        return ResultUtils.success(admin);
    }

    /**
     * 创建管理员
     *
     * @param admin 管理员信息
     * @return 创建结果
     */
    @ApiOperation(value = "创建管理员", notes = "创建新的管理员账号")
    @PostMapping("/create")
    public BaseResponse<Long> createAdmin(@ApiParam(value = "管理员信息", required = true) @RequestBody UrUsers admin) {
        if (admin.getUsername() == null || admin.getPasswordHash() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        // 检查用户名是否已存在
        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", admin.getUsername());
        long count = urUsersService.count(queryWrapper);
        if (count > 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        admin.setUserType(3);
        admin.setStatus(1);
        boolean save = urUsersService.save(admin);
        if (!save) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "创建失败");
        }
        return ResultUtils.success(admin.getId());
    }

    /**
     * 更新管理员
     *
     * @param admin 管理员信息
     * @return 更新结果
     */
    @ApiOperation(value = "更新管理员", notes = "更新管理员信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateAdmin(@ApiParam(value = "管理员信息", required = true) @RequestBody UrUsers admin) {
        if (admin.getId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "管理员ID不能为空");
        }
        UrUsers existing = urUsersService.getById(admin.getId());
        if (existing == null || existing.getUserType() != 3) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "管理员不存在");
        }

        // 如果修改了用户名，检查新用户名是否已存在
        if (!existing.getUsername().equals(admin.getUsername())) {
            QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", admin.getUsername());
            queryWrapper.ne("id", admin.getId());
            long count = urUsersService.count(queryWrapper);
            if (count > 0) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户名已存在");
            }
        }

        boolean update = urUsersService.updateById(admin);
        return ResultUtils.success(update);
    }

    /**
     * 删除管理员
     *
     * @param userId 管理员ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除管理员", notes = "删除管理员账号")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAdmin(@ApiParam(value = "管理员ID", required = true) @RequestParam Long userId) {
        UrUsers existing = urUsersService.getById(userId);
        if (existing == null || existing.getUserType() != 3) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "管理员不存在");
        }
        boolean delete = urUsersService.removeById(userId);
        return ResultUtils.success(delete);
    }

    /**
     * 导出管理员列表到Excel
     */
    @ApiOperation(value = "导出管理员列表", notes = "将管理员列表导出为Excel文件")
    @PostMapping("/export")
    public void exportAdmins(HttpServletResponse response) throws Exception {
        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", 3);
        List<UrUsers> admins = urUsersService.list(queryWrapper);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("管理员列表", "UTF-8") + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 使用EasyExcel导出
        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), UrUsers.class)
                .sheet("管理员列表")
                .doWrite(admins);
    }

}
