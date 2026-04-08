package com.springbootinit.controller.pro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.JbJobs;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.service.JbJobsService;
import com.springbootinit.service.UrUsersService;
import com.springbootinit.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 商家-兼职管理接口
 */
@Api(tags = "商家-兼职管理")
@RestController
@RequestMapping("/pro/job")
public class ProJobController {

    @Autowired
    private JbJobsService jbJobsService;

    @Autowired
    private UrUsersService urUsersService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取本商户的兼职列表（分页）
     *
     * @param requestBody 请求体，包含页码、每页大小和搜索参数
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 兼职列表
     */
    @ApiOperation(value = "获取本商户的兼职列表", notes = "分页获取本商户的兼职列表，支持搜索")
    @PostMapping("/list")
    public BaseResponse<Page<JbJobs>> getJobs(
            @ApiParam(value = "请求体", required = true) @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {
        // 从Cookie中获取token
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        // 验证token
        try {
            Long merchantId = jwtUtils.getUserIdFromToken(token);

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", merchantId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 获取分页参数
            Integer pageNum = (Integer) requestBody.get("pageNum");
            Integer pageSize = (Integer) requestBody.get("pageSize");
            if (pageNum == null || pageSize == null) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "分页参数不能为空");
            }

            // 获取搜索参数
            Map<String, Object> params = (Map<String, Object>) requestBody.get("params");

            // 查询本商户的兼职列表
            Page<JbJobs> pageInfo = new Page<>(pageNum, pageSize);
            QueryWrapper<JbJobs> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("merchant_id", merchantId);

            // 处理搜索参数
            if (params != null) {
                // 岗位标题
                if (params.get("title") != null && !params.get("title").toString().isEmpty()) {
                    queryWrapper.like("title", params.get("title"));
                }
                // 分类ID
                if (params.get("categoryId") != null && !params.get("categoryId").toString().isEmpty()) {
                    queryWrapper.eq("category_id", params.get("categoryId"));
                }
                // 地区代码
                if (params.get("regionCode") != null && !params.get("regionCode").toString().isEmpty()) {
                    queryWrapper.eq("region_code", params.get("regionCode"));
                }
                // 状态
                if (params.get("status") != null && !params.get("status").toString().isEmpty()) {
                    queryWrapper.eq("status", params.get("status"));
                }
                // 交易模式
                if (params.get("tradeMode") != null && !params.get("tradeMode").toString().isEmpty()) {
                    queryWrapper.eq("trade_mode", params.get("tradeMode"));
                }
                // 最低薪资
                if (params.get("salaryMin") != null && !params.get("salaryMin").toString().isEmpty()) {
                    queryWrapper.ge("salary_min", params.get("salaryMin"));
                }
                // 最高薪资
                if (params.get("salaryMax") != null && !params.get("salaryMax").toString().isEmpty()) {
                    queryWrapper.le("salary_max", params.get("salaryMax"));
                }
            }

            Page<JbJobs> result = jbJobsService.page(pageInfo, queryWrapper);
            return ResultUtils.success(result);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 获取兼职详情
     *
     * @param jobId 兼职ID
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 兼职详情
     */
    @ApiOperation(value = "获取兼职详情", notes = "根据ID获取兼职详细信息")
    @PostMapping("/detail")
    public BaseResponse<JbJobs> getJob(
            @ApiParam(value = "兼职ID", required = true) @RequestParam Long jobId,
            HttpServletRequest request) {
        // 从Cookie中获取token
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        // 验证token
        try {
            Long merchantId = jwtUtils.getUserIdFromToken(token);

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", merchantId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 查询兼职详情
            JbJobs job = jbJobsService.getById(jobId);
            if (job == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职不存在");
            }

            // 验证是否是本商户的兼职
            if (!job.getMerchantId().equals(merchantId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权访问此兼职");
            }

            return ResultUtils.success(job);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 发布新兼职
     *
     * @param job 兼职信息
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 创建结果
     */
    @ApiOperation(value = "发布新兼职", notes = "发布新的兼职岗位")
    @PostMapping("/create")
    public BaseResponse<Long> createJob(
            @ApiParam(value = "兼职信息", required = true) @RequestBody JbJobs job,
            HttpServletRequest request) {
        // 从Cookie中获取token
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        // 验证token
        try {
            Long merchantId = jwtUtils.getUserIdFromToken(token);

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", merchantId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 设置商户ID和发布时间
            job.setMerchantId(merchantId);
            job.setPublishTime(new Date());
            job.setStatus(1); // 发布中

            // 保存兼职信息
            boolean save = jbJobsService.save(job);
            if (!save) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "发布失败");
            }

            return ResultUtils.success(job.getId());
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 更新兼职信息
     *
     * @param job 兼职信息
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 更新结果
     */
    @ApiOperation(value = "更新兼职信息", notes = "更新兼职岗位信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateJob(
            @ApiParam(value = "兼职信息", required = true) @RequestBody JbJobs job,
            HttpServletRequest request) {
        // 从Cookie中获取token
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        // 验证token
        try {
            Long merchantId = jwtUtils.getUserIdFromToken(token);

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", merchantId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 验证兼职是否存在
            JbJobs existingJob = jbJobsService.getById(job.getId());
            if (existingJob == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职不存在");
            }

            // 验证是否是本商户的兼职
            if (!existingJob.getMerchantId().equals(merchantId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权修改此兼职");
            }

            // 更新兼职信息
            boolean update = jbJobsService.updateById(job);
            return ResultUtils.success(update);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 结款功能
     *
     * @param jobId 兼职ID
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 结款结果
     */
    @ApiOperation(value = "结款", notes = "为兼职岗位进行结款")
    @PostMapping("/settle")
    public BaseResponse<Boolean> settleJob(
            @ApiParam(value = "兼职ID", required = true) @RequestParam Long jobId,
            HttpServletRequest request) {
        // 从Cookie中获取token
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        // 验证token
        try {
            Long merchantId = jwtUtils.getUserIdFromToken(token);

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", merchantId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 验证兼职是否存在
            JbJobs job = jbJobsService.getById(jobId);
            if (job == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职不存在");
            }

            // 验证是否是本商户的兼职
            if (!job.getMerchantId().equals(merchantId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作此兼职");
            }

            // 更新兼职状态为已完成
            job.setStatus(3); // 已完成
            boolean update = jbJobsService.updateById(job);
            return ResultUtils.success(update);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 退押金功能
     *
     * @param jobId 兼职ID
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 退押金结果
     */
    @ApiOperation(value = "退押金", notes = "为兼职岗位退还押金")
    @PostMapping("/refundDeposit")
    public BaseResponse<Boolean> refundDeposit(
            @ApiParam(value = "兼职ID", required = true) @RequestParam Long jobId,
            HttpServletRequest request) {
        // 从Cookie中获取token
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        // 验证token
        try {
            Long merchantId = jwtUtils.getUserIdFromToken(token);

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", merchantId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 验证兼职是否存在
            JbJobs job = jbJobsService.getById(jobId);
            if (job == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职不存在");
            }

            // 验证是否是本商户的兼职
            if (!job.getMerchantId().equals(merchantId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作此兼职");
            }

            // 这里可以添加退押金的具体逻辑，比如更新订单状态、处理资金等
            // 目前仅返回成功
            return ResultUtils.success(true);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 从Cookie中获取token
     *
     * @param request 请求对象
     * @return token
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        String token = null;
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
}
