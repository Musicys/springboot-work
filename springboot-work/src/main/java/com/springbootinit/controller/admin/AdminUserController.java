package com.springbootinit.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.model.dto.UserSearchWrapper;
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
 * 管理员-用户管理接口
 */
@Api(tags = "管理员-用户管理")
@RestController
@RequestMapping("/admin/yhgl")
public class AdminUserController {

    @Autowired
    private UrUsersService urUsersService;

    /**
     * 获取用户列表（分页）
     *
     * @param searchWrapper 搜索包装参数
     * @return 用户列表
     */
    @ApiOperation(value = "获取用户列表", notes = "分页获取用户列表，支持按多个字段搜索")
    @PostMapping("/list")
    public BaseResponse<Page<UrUsers>> getUsers(
            @ApiParam(value = "搜索包装参数") @RequestBody UserSearchWrapper searchWrapper) {
        Page<UrUsers> pageInfo = new Page<>(searchWrapper.getPage().getPageNum(), searchWrapper.getPage().getPageSize());
        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", 1); // 用户类型
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
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    @ApiOperation(value = "获取用户详情", notes = "根据ID获取用户详细信息")
    @PostMapping("/detail")
    public BaseResponse<UrUsers> getUser(@ApiParam(value = "用户ID", required = true) @RequestParam Long id) {
        UrUsers user = urUsersService.getById(id);
        if (user == null || user.getUserType() != 1) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return ResultUtils.success(user);
    }

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 创建结果
     */
    @ApiOperation(value = "创建用户", notes = "创建新的用户账号")
    @PostMapping("/create")
    public BaseResponse<Long> createUser(@ApiParam(value = "用户信息", required = true) @RequestBody UrUsers user) {
        if (user.getUsername() == null || user.getPasswordHash() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        user.setUserType(1);
        user.setStatus(1);
        boolean save = urUsersService.save(user);
        if (!save) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "创建失败");
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 更新结果
     */
    @ApiOperation(value = "更新用户", notes = "更新用户信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@ApiParam(value = "用户信息", required = true) @RequestBody UrUsers user) {
        if (user.getId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        UrUsers existing = urUsersService.getById(user.getId());
        if (existing == null || existing.getUserType() != 1) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        boolean update = urUsersService.updateById(user);
        return ResultUtils.success(update);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除用户", notes = "删除用户账号")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@ApiParam(value = "用户ID", required = true) @RequestParam Long id) {
        UrUsers existing = urUsersService.getById(id);
        if (existing == null || existing.getUserType() != 1) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        boolean delete = urUsersService.removeById(id);
        return ResultUtils.success(delete);
    }

    /**
     * 导出用户列表到Excel
     */
    @ApiOperation(value = "导出用户列表", notes = "将用户列表导出为Excel文件")
    @PostMapping("/export")
    public void exportUsers(HttpServletResponse response) throws Exception {
        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", 1);
        List<UrUsers> users = urUsersService.list(queryWrapper);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("用户列表", "UTF-8") + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 使用EasyExcel导出
        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), UrUsers.class)
                .sheet("用户列表")
                .doWrite(users);
    }

}
