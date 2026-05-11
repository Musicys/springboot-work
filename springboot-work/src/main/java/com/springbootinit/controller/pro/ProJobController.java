package com.springbootinit.controller.pro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.model.domain.*;
import com.springbootinit.model.dto.JobQueryDTO;
import com.springbootinit.model.dto.ProJobPublishRequest;
import com.springbootinit.model.vo.JobDetailVO;
import com.springbootinit.model.vo.JobVO;
import com.springbootinit.model.vo.OrderVO;
import com.springbootinit.service.*;
import com.springbootinit.utils.JwtUtils;
import com.springbootinit.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家端兼职接口
 */
@Api(tags = "商家端兼职接口")
@RestController
@RequestMapping("/pro/job")
public class ProJobController {

    @Autowired
    private JbJobsService jbJobsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    @Autowired
    private OdOrdersService odOrdersService;

    @Autowired
    private OdApplicationsService odApplicationsService;

    @Autowired
    private UrUsersService urUsersService;

    @Autowired
    private UrIntentionsService urIntentionService;

    @Autowired
    private WlWalletsService wlWalletsService;

    @Autowired
    private WlTransactionLogsService wlTransactionLogsService;

    private Long getUserIdFromCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Merchant-Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null) {
            return null;
        }
        try {
            return jwtUtils.getUserIdFromToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    @ApiOperation(value = "发布兼职", notes = "商家发布新的兼职岗位")
    @PostMapping("/publish")
    public BaseResponse<Long> publishJob(@RequestBody ProJobPublishRequest request, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromCookie(httpRequest);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        JbJobs job = new JbJobs();
        job.setMerchantId(userId);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setCategoryId(Math.toIntExact(request.getCategoryId()));
        job.setRegionCode(request.getRegionCode());
        job.setRegionName(request.getRegionName());
        job.setDepositAmount(request.getDepositAmount() != null ? request.getDepositAmount() : BigDecimal.ZERO);
        job.setSettlementCycle(Integer.valueOf(request.getSettlementCycle()));
        job.setWorkTimeType(request.getWorkTimeType());
        job.setWorkTimeDesc(request.getWorkTimeDesc());
        job.setJobType(request.getJobType());
        job.setRecruitNum(request.getRecruitNum());
        job.setBriefIntro(request.getBriefIntro());
        job.setTradeMode(request.getTradeMode() != null ? request.getTradeMode() : 1);
        job.setJobStatus(0);
        job.setStatus(1);
        job.setPublishTime(new java.util.Date());

        boolean saved = jbJobsService.save(job);
        if (!saved) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "发布失败");
        }

        return ResultUtils.success(job.getId());
    }

    @ApiOperation(value = "待审核岗位列表", notes = "获取当前商家待审核的兼职岗位列表")
    @PostMapping("/list/pending")
    public BaseResponse<Page<JobVO>> listPendingJobs(
            @RequestBody JobQueryDTO queryDTO,
            HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        Page<JbJobs> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        QueryWrapper<JbJobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchant_id", userId);
        queryWrapper.eq("job_status", 0);

        String keyword = queryDTO.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("title", keyword);
        }

        queryWrapper.orderByDesc("created_at");
        Page<JbJobs> jobPage = jbJobsService.page(page, queryWrapper);

        Page<JobVO> resultPage = new Page<>(jobPage.getCurrent(), jobPage.getSize(), jobPage.getTotal());
        resultPage.setRecords(jobPage.getRecords().stream().map(job -> {
            JobVO vo = new JobVO();
            BeanUtils.copyProperties(job, vo);
            vo.setId(job.getId());
            return vo;
        }).collect(Collectors.toList()));

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "进行中岗位列表", notes = "获取当前商家进行中的兼职岗位列表(job_status=1, status=2)")
    @PostMapping("/list/active")
    public BaseResponse<Page<JobVO>> listActiveJobs(
            @RequestBody JobQueryDTO queryDTO,
            HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        Page<JbJobs> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        QueryWrapper<JbJobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchant_id", userId);
        queryWrapper.eq("job_status", 1);
        queryWrapper.eq("status", 2);

        String keyword = queryDTO.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("title", keyword);
        }

        queryWrapper.orderByDesc("created_at");
        Page<JbJobs> jobPage = jbJobsService.page(page, queryWrapper);

        Page<JobVO> resultPage = new Page<>(jobPage.getCurrent(), jobPage.getSize(), jobPage.getTotal());
        resultPage.setRecords(jobPage.getRecords().stream().map(job -> {
            JobVO vo = new JobVO();
            BeanUtils.copyProperties(job, vo);
            vo.setId(job.getId());
            return vo;
        }).collect(Collectors.toList()));

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "岗位详情", notes = "获取兼职岗位详细信息")
    @GetMapping("/{id}")
    public BaseResponse<JobDetailVO> getJobDetail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        JbJobs job = jbJobsService.getById(id);
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "岗位不存在");
        }

        if (!job.getMerchantId().equals(userId)) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权查看该岗位");
        }

        JobDetailVO vo = new JobDetailVO();
        BeanUtils.copyProperties(job, vo);

        return ResultUtils.success(vo);
    }

    @ApiOperation(value = "删除岗位", notes = "删除发布的兼职岗位")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteJob(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        JbJobs job = jbJobsService.getById(id);
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "岗位不存在");
        }

        if (!job.getMerchantId().equals(userId)) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权删除该岗位");
        }

        boolean removed = jbJobsService.removeById(id);
        return ResultUtils.success(removed);
    }

    @ApiOperation(value = "获取所有待审核的应聘列表", notes = "获取当前商家所有待审核的应聘订单信息")
    @GetMapping("/applications/all")
    public BaseResponse<List<Map<String, Object>>> getAllApplications(
            javax.servlet.http.HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);

            // 查询当前商家的所有待审核订单（order_status=0）
            QueryWrapper<OdOrders> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("merchant_id", userId);
            orderQueryWrapper.eq("order_status", 0);
            orderQueryWrapper.orderByDesc("created_at");
            List<OdOrders> orders = odOrdersService.list(orderQueryWrapper);

            List<Map<String, Object>> result = new ArrayList<>();
            for (OdOrders order : orders) {
                Map<String, Object> application = new HashMap<>();
                application.put("orderId", order.getId());
                application.put("jobId", order.getJobId());
                application.put("orderStatus", order.getOrderStatus());
                application.put("createdAt", order.getCreatedAt());

                // 查询兼职信息
                JbJobs job = jbJobsService.getById(order.getJobId());
                if (job != null) {
                    application.put("jobTitle", job.getTitle());
                    application.put("jobStatus", job.getStatus());
                    application.put("jobJobStatus", job.getJobStatus());
                    application.put("salaryMin", job.getSalaryMin());
                    application.put("salaryMax", job.getSalaryMax());
                }

                // 查询用户信息
                UrUsers user = urUsersService.getById(order.getUserId());
                if (user != null) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId());
                    userInfo.put("username", user.getUsername());
                    userInfo.put("avatarUrl", user.getAvatarUrl());
                    userInfo.put("creditCode", user.getCreditScore());
                    application.put("user", userInfo);

                    // 查询求职意向信息
                    QueryWrapper<UrIntentions> intentionQueryWrapper = new QueryWrapper<>();
                    intentionQueryWrapper.eq("user_id", order.getUserId());
                    UrIntentions intentions = urIntentionService.getOne(intentionQueryWrapper);
                    if (intentions != null) {
                        Map<String, Object> intentionInfo = new HashMap<>();
                        intentionInfo.put("realName", intentions.getRealName());
                        intentionInfo.put("studentId", intentions.getStudentId());
                        intentionInfo.put("age", intentions.getAge());
                        intentionInfo.put("gender", intentions.getGender());
                        intentionInfo.put("phone", intentions.getPhone());
                        intentionInfo.put("profession", intentions.getProfession());
                        intentionInfo.put("introduction", intentions.getIntroduction());
                        intentionInfo.put("tagName", intentions.getTagName());
                        application.put("intention", intentionInfo);
                    }
                }

                result.add(application);
            }

            return ResultUtils.success(result);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    @ApiOperation(value = "获取所有待入职的应聘列表", notes = "获取当前商家所有待入职的应聘订单信息")
    @GetMapping("/applications/pending-entry")
    public BaseResponse<List<Map<String, Object>>> getPendingEntryApplications(
            javax.servlet.http.HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);

            // 查询当前商家的所有待入职订单（order_status=1）
            QueryWrapper<OdOrders> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("merchant_id", userId);
            orderQueryWrapper.eq("order_status", 1);
            orderQueryWrapper.orderByDesc("created_at");
            List<OdOrders> orders = odOrdersService.list(orderQueryWrapper);

            List<Map<String, Object>> result = new ArrayList<>();
            for (OdOrders order : orders) {
                Map<String, Object> application = new HashMap<>();
                application.put("orderId", order.getId());
                application.put("jobId", order.getJobId());
                application.put("orderStatus", order.getOrderStatus());
                application.put("createdAt", order.getCreatedAt());

                // 查询兼职信息
                JbJobs job = jbJobsService.getById(order.getJobId());
                if (job != null) {
                    application.put("jobTitle", job.getTitle());
                    application.put("jobStatus", job.getStatus());
                    application.put("jobJobStatus", job.getJobStatus());
                }

                // 查询用户信息
                UrUsers user = urUsersService.getById(order.getUserId());
                if (user != null) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId());
                    userInfo.put("username", user.getUsername());
                    userInfo.put("avatarUrl", user.getAvatarUrl());
                    userInfo.put("creditCode", user.getCreditScore());
                    application.put("user", userInfo);

                    // 查询求职意向信息
                    QueryWrapper<UrIntentions> intentionQueryWrapper = new QueryWrapper<>();
                    intentionQueryWrapper.eq("user_id", order.getUserId());
                    UrIntentions intentions = urIntentionService.getOne(intentionQueryWrapper);
                    if (intentions != null) {
                        Map<String, Object> intentionInfo = new HashMap<>();
                        intentionInfo.put("realName", intentions.getRealName());
                        intentionInfo.put("studentId", intentions.getStudentId());
                        intentionInfo.put("age", intentions.getAge());
                        intentionInfo.put("gender", intentions.getGender());
                        intentionInfo.put("phone", intentions.getPhone());
                        intentionInfo.put("profession", intentions.getProfession());
                        intentionInfo.put("introduction", intentions.getIntroduction());
                        intentionInfo.put("tagName", intentions.getTagName());
                        application.put("intention", intentionInfo);
                    }
                }

                result.add(application);
            }

            return ResultUtils.success(result);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    @ApiOperation(value = "同意应聘", notes = "商家同意用户的应聘，冻结薪资金额，将订单状态改为待入职")
    @PostMapping("/approveApplication")
    public BaseResponse<Boolean> approveApplication(
            javax.servlet.http.HttpServletRequest request,
            @RequestBody Map<String, Object> requestBody) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);
            Long orderId = Long.valueOf(requestBody.get("orderId").toString());
            BigDecimal freezeAmount = new BigDecimal(requestBody.get("freezeAmount").toString());

            if (orderId == null) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
            }

            if (freezeAmount == null || freezeAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "冻结金额必须大于0");
            }

            OdOrders order = odOrdersService.getById(orderId);
            if (order == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
            }

            if (!order.getMerchantId().equals(userId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作该订单");
            }

            if (order.getOrderStatus() != 0) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该订单已处理");
            }

            // 查询商家钱包
            WlWallets wallet = wlWalletsService.getById(userId);
            if (wallet == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "钱包不存在");
            }

            // 检查余额是否充足
            if (wallet.getBalance().compareTo(freezeAmount) < 0) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "余额不足，无法冻结薪资金额");
            }

            // 冻结金额：余额 - 冻结金额，冻结金额 + 冻结金额
            BigDecimal newBalance = wallet.getBalance().subtract(freezeAmount);
            BigDecimal newFrozenBalance = wallet.getFrozenBalance().add(freezeAmount);

            wallet.setBalance(newBalance);
            wallet.setFrozenBalance(newFrozenBalance);
            wallet.setUpdatedAt(new java.util.Date());

            boolean walletUpdated = wlWalletsService.updateById(wallet);
            if (!walletUpdated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "钱包更新失败");
            }

            // 更新订单状态为待入职（1）
            order.setOrderStatus(1);
            boolean orderUpdated = odOrdersService.updateById(order);
            if (!orderUpdated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "订单更新失败");
            }

            return ResultUtils.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "操作失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "获取进行中订单列表", notes = "获取当前商家所有进行中的订单（order_status=2）")
    @GetMapping("/orders/in-progress")
    public BaseResponse<List<Map<String, Object>>> getInProgressOrders(
            javax.servlet.http.HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);

            // 查询当前商家的所有进行中订单（order_status=2）
            QueryWrapper<OdOrders> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("merchant_id", userId);
            orderQueryWrapper.eq("order_status", 2);
            orderQueryWrapper.orderByDesc("created_at");
            List<OdOrders> orders = odOrdersService.list(orderQueryWrapper);

            List<Map<String, Object>> result = new ArrayList<>();
            for (OdOrders order : orders) {
                Map<String, Object> application = new HashMap<>();
                application.put("orderId", order.getId());
                application.put("jobId", order.getJobId());
                application.put("orderStatus", order.getOrderStatus());
                application.put("createdAt", order.getCreatedAt());
                application.put("startTime", order.getStartTime());
                application.put("isDepositRefunded", order.getIsDepositRefunded());
                application.put("isSettled", order.getIsSettled());

                // 查询兼职信息
                JbJobs job = jbJobsService.getById(order.getJobId());
                if (job != null) {
                    application.put("jobTitle", job.getTitle());
                    application.put("salaryMin", job.getSalaryMin());
                    application.put("salaryMax", job.getSalaryMax());
                    application.put("frozenAmount", job.getDepositAmount());
                }

                // 查询用户信息
                UrUsers user = urUsersService.getById(order.getUserId());
                if (user != null) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId());
                    userInfo.put("username", user.getUsername());
                    userInfo.put("avatarUrl", user.getAvatarUrl());
                    userInfo.put("creditCode", user.getCreditScore());
                    application.put("user", userInfo);

                    // 查询求职意向信息
                    QueryWrapper<UrIntentions> intentionQueryWrapper = new QueryWrapper<>();
                    intentionQueryWrapper.eq("user_id", order.getUserId());
                    UrIntentions intentions = urIntentionService.getOne(intentionQueryWrapper);
                    if (intentions != null) {
                        Map<String, Object> intentionInfo = new HashMap<>();
                        intentionInfo.put("realName", intentions.getRealName());
                        intentionInfo.put("studentId", intentions.getStudentId());
                        intentionInfo.put("age", intentions.getAge());
                        intentionInfo.put("gender", intentions.getGender());
                        intentionInfo.put("phone", intentions.getPhone());
                        intentionInfo.put("profession", intentions.getProfession());
                        intentionInfo.put("introduction", intentions.getIntroduction());
                        intentionInfo.put("tagName", intentions.getTagName());
                        application.put("intention", intentionInfo);
                    }
                }

                result.add(application);
            }

            return ResultUtils.success(result);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }




    @ApiOperation(value = "结款", notes = "商家给用户结款，包含退押和结款操作，更新订单状态为已完成")
    @PostMapping("/orders/settle")
    public BaseResponse<Boolean> settleOrder(
            javax.servlet.http.HttpServletRequest request,
            @RequestBody Map<String, Object> requestBody) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);
            Long orderId = Long.valueOf(requestBody.get("orderId").toString());

            if (orderId == null) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
            }

            OdOrders order = odOrdersService.getById(orderId);
            if (order == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
            }

            if (!order.getMerchantId().equals(userId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作该订单");
            }

            if (order.getOrderStatus() != 2) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该订单状态不允许结款");
            }

            // 查询兼职信息获取押金金额
            JbJobs job = jbJobsService.getById(order.getJobId());
            if (job == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职岗位不存在");
            }

            BigDecimal depositAmount = job.getDepositAmount();
            if (depositAmount == null) {
                depositAmount = BigDecimal.ZERO;
            }

            // 查询商家钱包
            WlWallets merchantWallet = wlWalletsService.getById(userId);
            if (merchantWallet == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "商家钱包不存在");
            }

            // 查询用户钱包
            WlWallets userWallet = wlWalletsService.getById(order.getUserId());
            if (userWallet == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "用户钱包不存在");
            }

            // 保存更新前的余额用于交易记录
            BigDecimal merchantBalanceBefore = merchantWallet.getFrozenBalance();
            BigDecimal userBalanceBefore = userWallet.getBalance();
            BigDecimal userFrozenBefore = userWallet.getFrozenBalance();

            // 1. 商家冻结金额 - 结算金额
            BigDecimal newMerchantFrozen = merchantWallet.getFrozenBalance().subtract(depositAmount);
            if (newMerchantFrozen.compareTo(BigDecimal.ZERO) < 0) {
                newMerchantFrozen = BigDecimal.ZERO;
            }
            merchantWallet.setFrozenBalance(newMerchantFrozen);

            // 2. 用户余额 + 结算金额 + 解冻押金
            BigDecimal newUserBalance = userWallet.getBalance().add(depositAmount).add(userWallet.getFrozenBalance());
            userWallet.setBalance(newUserBalance);
            userWallet.setFrozenBalance(BigDecimal.ZERO); // 解冻用户押金

            boolean merchantUpdated = wlWalletsService.updateById(merchantWallet);
            boolean userUpdated = wlWalletsService.updateById(userWallet);

            if (!merchantUpdated || !userUpdated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "钱包更新失败");
            }

            // 3. 添加交易流水记录（用户端 - 结款收入）
            WlTransactionLogs userTransaction = new WlTransactionLogs();
            userTransaction.setUserId(order.getUserId());
            userTransaction.setType("SALARY_PAY");
            userTransaction.setAmount(depositAmount);
            userTransaction.setBalanceBefore(userBalanceBefore);
            userTransaction.setBalanceAfter(userWallet.getBalance().subtract(userFrozenBefore));
            userTransaction.setRelatedOrderId(orderId);
            userTransaction.setDescription("兼职薪资结算");
            userTransaction.setCreatedAt(new java.util.Date());
            userTransaction.setOperatorId(order.getMerchantId());
            wlTransactionLogsService.save(userTransaction);

            // 4. 添加交易流水记录（用户端 - 押金退还）
            WlTransactionLogs refundTransaction = new WlTransactionLogs();
            refundTransaction.setUserId(order.getUserId());
            refundTransaction.setType("REFUND");
            refundTransaction.setAmount(userFrozenBefore);
            refundTransaction.setBalanceBefore(userWallet.getBalance().subtract(userFrozenBefore));
            refundTransaction.setBalanceAfter(userWallet.getBalance());
            refundTransaction.setRelatedOrderId(orderId);
            refundTransaction.setDescription("押金退还");
            refundTransaction.setCreatedAt(new java.util.Date());
            refundTransaction.setOperatorId(order.getMerchantId());
            wlTransactionLogsService.save(refundTransaction);

            // 5. 更新订单状态为已完成（3），并标记已退押和已结款
            order.setOrderStatus(3);
            order.setIsDepositRefunded(1);
            order.setIsSettled(1);
            order.setCompletedAt(new java.util.Date());
            boolean orderUpdated = odOrdersService.updateById(order);
            if (!orderUpdated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "订单更新失败");
            }

            return ResultUtils.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "操作失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "退押", notes = "商家退还用户押金，解冻冻结金额")
    @PostMapping("/orders/refund")
    public BaseResponse<Boolean> refundDeposit(
            javax.servlet.http.HttpServletRequest request,
            @RequestBody Map<String, Object> requestBody) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);
            Long orderId = Long.valueOf(requestBody.get("orderId").toString());

            if (orderId == null) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
            }

            OdOrders order = odOrdersService.getById(orderId);
            if (order == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
            }

            if (!order.getMerchantId().equals(userId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作该订单");
            }

            if (order.getOrderStatus() != 2) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该订单状态不允许退押");
            }

            // 查询兼职信息获取押金金额
            JbJobs job = jbJobsService.getById(order.getJobId());
            if (job == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职岗位不存在");
            }

            BigDecimal refundAmount = job.getDepositAmount();
            if (refundAmount == null) {
                refundAmount = BigDecimal.ZERO;
            }

            // 查询用户钱包
            WlWallets userWallet = wlWalletsService.getById(order.getUserId());
            if (userWallet == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "用户钱包不存在");
            }

            // 用户冻结金额 - 押金，用户余额 + 押金
            BigDecimal newFrozenBalance = userWallet.getFrozenBalance().subtract(refundAmount);
            if (newFrozenBalance.compareTo(BigDecimal.ZERO) < 0) {
                newFrozenBalance = BigDecimal.ZERO;
            }
            BigDecimal newBalance = userWallet.getBalance().add(refundAmount);

            userWallet.setFrozenBalance(newFrozenBalance);
            userWallet.setBalance(newBalance);
            userWallet.setUpdatedAt(new java.util.Date());

            boolean walletUpdated = wlWalletsService.updateById(userWallet);
            if (!walletUpdated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "钱包更新失败");
            }

            // 更新订单的退押状态为已退押（1），不改变订单状态
            order.setIsDepositRefunded(1);
            boolean orderUpdated = odOrdersService.updateById(order);
            if (!orderUpdated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "订单更新失败");
            }

            // 添加交易流水记录
            WlTransactionLogs transactionLog = new WlTransactionLogs();
            transactionLog.setUserId(order.getUserId());
            transactionLog.setOperatorId(userId);
            transactionLog.setType("REFUND");
            transactionLog.setAmount(refundAmount);
            transactionLog.setBalanceBefore(userWallet.getBalance().subtract(refundAmount));
            transactionLog.setBalanceAfter(userWallet.getBalance());
            transactionLog.setRelatedOrderId(orderId);
            transactionLog.setDescription("押金退还");
            wlTransactionLogsService.save(transactionLog);

            return ResultUtils.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "操作失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "异议申请", notes = "商家对订单发起异议")
    @PostMapping("/orders/dispute")
    public BaseResponse<Boolean> applyDispute(
            javax.servlet.http.HttpServletRequest request,
            @RequestBody Map<String, Object> requestBody) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);
            Long orderId = Long.valueOf(requestBody.get("orderId").toString());

            if (orderId == null) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
            }

            OdOrders order = odOrdersService.getById(orderId);
            if (order == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
            }

            if (!order.getMerchantId().equals(userId)) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作该订单");
            }

            if (order.getOrderStatus() != 2) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该订单状态不允许发起异议");
            }

            // 更新订单状态为纠纷中（4）
            order.setOrderStatus(4);
            boolean orderUpdated = odOrdersService.updateById(order);
            if (!orderUpdated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "订单更新失败");
            }

            return ResultUtils.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "操作失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "获取订单列表", notes = "获取当前商家的订单列表，支持分页和筛选，包含用户信息和兼职岗位信息")
    @GetMapping("/orders/list")
    public BaseResponse<Page<OrderVO>> getOrders(
            javax.servlet.http.HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) Integer tradeMode) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        try {
            Long userId = jwtUtils.getUserIdFromToken(token);

            Page<OdOrders> page = new Page<>(pageNum, pageSize);
            QueryWrapper<OdOrders> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("merchant_id", userId);

            if (StringUtils.isNotBlank(orderId)) {
                queryWrapper.like("id", orderId);
            }
            if (orderStatus != null) {
                queryWrapper.eq("order_status", orderStatus);
            }
            if (tradeMode != null) {
                queryWrapper.eq("trade_mode", tradeMode);
            }

            queryWrapper.orderByDesc("created_at");
            Page<OdOrders> result = odOrdersService.page(page, queryWrapper);

            // 转换为VO并联查用户和岗位信息
            List<OrderVO> voList = new ArrayList<>();
            for (OdOrders order : result.getRecords()) {
                OrderVO vo = OrderVO.fromOrder(order);

                // 联查用户信息
                QueryWrapper<UrIntentions> intentionQuery = new QueryWrapper<>();
                intentionQuery.eq("user_id", order.getUserId());
                UrIntentions intention = urIntentionService.getOne(intentionQuery);
                if (intention != null) {
                    vo.setStudentId(intention.getStudentId());
                    vo.setRealName(intention.getRealName());
                    vo.setAge(intention.getAge());
                    vo.setGender(intention.getGender());
                    vo.setPhone(intention.getPhone());
                    vo.setProfession(intention.getProfession());
                    vo.setIntroduction(intention.getIntroduction());
                    vo.setTagName(intention.getTagName());

                }

                // 联查兼职岗位信息
                JbJobs job = jbJobsService.getById(order.getJobId());
                if (job != null) {
                    vo.setJobTitle(job.getTitle());
                    vo.setJobBriefIntro(job.getBriefIntro());
                    vo.setJobSalaryMin(job.getSalaryMin());
                    vo.setJobSalaryMax(job.getSalaryMax());
                    vo.setJobRegionName(job.getRegionName());
                }

                voList.add(vo);
            }

            Page<OrderVO> voPage = new Page<>();
            voPage.setRecords(voList);
            voPage.setSize(result.getSize());
            voPage.setTotal(result.getTotal());
            voPage.setCurrent(result.getCurrent());

            return ResultUtils.success(voPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "操作失败: " + e.getMessage());
        }
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Merchant-Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
