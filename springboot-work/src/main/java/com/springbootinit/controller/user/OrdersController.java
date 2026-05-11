package com.springbootinit.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.JbJobs;
import com.springbootinit.model.domain.OdOrders;
import com.springbootinit.model.domain.UrMerchantProfiles;
import com.springbootinit.model.domain.WlWallets;
import com.springbootinit.model.vo.OrderVO;
import com.springbootinit.service.JbJobsService;
import com.springbootinit.service.OdOrdersService;
import com.springbootinit.service.UrMerchantProfilesService;
import com.springbootinit.service.WlWalletsService;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户订单接口
 */
@Api(tags = "用户订单接口")
@RestController
@RequestMapping("/user/orders")
public class OrdersController {

    @Autowired
    private OdOrdersService odOrdersService;

    @Autowired
    private JbJobsService jbJobsService;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    @Autowired
    private WlWalletsService wlWalletsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 从Cookie中获取用户ID
     */
    private Long getUserIdFromCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("User-Authorization".equals(cookie.getName())) {
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

    @Autowired(required = false)
    private com.springbootinit.utils.JwtUtils jwtUtils;

    /**
     * 获取订单状态描述
     */
    private String getOrderStatusDesc(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待商家审核";
            case 1: return "待入职";
            case 2: return "进行中";
            case 3: return "已完成";
            case 4: return "纠纷中";
            case 5: return "已结款";
            case 6: return "用户爽约";
            case 7: return "异常终止";
            default: return "未知";
        }
    }

    /**
     * 获取交易模式描述
     */
    private String getTradeModeDesc(Integer mode) {
        if (mode == null) return "未知";
        return mode == 1 ? "平台担保" : "线下交易";
    }

    /**
     * 获取兼职类型描述
     */
    private String getJobTypeDesc(Integer jobType) {
        if (jobType == null) return "未知";
        switch (jobType) {
            case 1: return "线上";
            case 2: return "线下";
            case 3: return "校园兼职";
            case 4: return "校外兼职";
            default: return "未知";
        }
    }

    /**
     * 兼职申请接口
     * 用户提交兼职ID和用户ID，直接创建订单记录，状态为0（待商家审核）
     */
    @ApiOperation(value = "兼职申请", notes = "用户提交兼职申请，直接创建订单记录，状态默认为0（待商家审核）")
    @PostMapping("/apply")
    public BaseResponse<Long> applyJob(@RequestBody Map<String, Long> request, HttpServletRequest httpRequest) {
        Long jobId = request.get("jobId");
        Long userId = request.get("userId");
        if (jobId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职ID不能为空");
        }
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        // 检查兼职是否存在
        JbJobs job = jbJobsService.getById(jobId);
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职岗位不存在");
        }

        // 检查是否已有订单（避免重复申请）
        QueryWrapper<OdOrders> orderQuery = new QueryWrapper<>();
        orderQuery.eq("job_id", jobId);
        orderQuery.eq("user_id", userId);
        OdOrders existingOrder = odOrdersService.getOne(orderQuery);
        if (existingOrder != null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "您已申请过该兼职");
        }

        // 直接创建订单记录，状态为0（待商家审核）
        OdOrders order = new OdOrders();
        order.setApplicationId(0L);
        order.setJobId(jobId);
        order.setUserId(userId);
        order.setMerchantId(job.getMerchantId());
        order.setTradeMode(job.getTradeMode() != null ? job.getTradeMode() : 1);
        order.setOrderStatus(0);

        boolean saved = odOrdersService.save(order);
        if (!saved) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "申请失败");
        }

        return ResultUtils.success(order.getId());
    }

    /**
     * 用户订单列表接口
     * 查询状态不为0的订单，联查兼职表和商家信息
     */
    @ApiOperation(value = "用户订单列表", notes = "获取用户的订单列表，联查兼职信息和商家信息，只返回状态不为0的订单")
    @GetMapping("/list")
    public BaseResponse<Page<OrderVO>> getOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer orderStatus,
            HttpServletRequest request) {

        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        Page<OdOrders> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OdOrders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (orderStatus != null) {
            queryWrapper.eq("order_status", orderStatus);
        }
        queryWrapper.orderByDesc("created_at");

        Page<OdOrders> orderPage = odOrdersService.page(page, queryWrapper);

        Page<OrderVO> resultPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        resultPage.setRecords(orderPage.getRecords().stream().map(order -> {
            OrderVO vo = new OrderVO();
            vo.setId(order.getId());
            vo.setJobId(order.getJobId());
            vo.setOrderStatus(order.getOrderStatus());
            vo.setOrderStatusDesc(getOrderStatusDesc(order.getOrderStatus()));
            vo.setTradeMode(order.getTradeMode());
            vo.setTradeModeDesc(getTradeModeDesc(order.getTradeMode()));
            vo.setCreatedAt(order.getCreatedAt());
            vo.setStartTime(order.getStartTime());
            vo.setEndTime(order.getEndTime());
            vo.setIsDepositRefunded(order.getIsDepositRefunded());
            vo.setIsSettled(order.getIsSettled());

            JbJobs job = jbJobsService.getById(order.getJobId());
            if (job != null) {
                vo.setJobTitle(job.getTitle());
                vo.setSalary(job.getSalaryMax() != null ? job.getSalaryMax() : job.getSalaryMin());
                vo.setDepositAmount(job.getDepositAmount());

                if (job.getCoverImages() != null && !job.getCoverImages().isEmpty()) {
                    try {
                        List<String> images = objectMapper.readValue(job.getCoverImages(), new TypeReference<List<String>>() {});
                        if (!images.isEmpty()) {
                            vo.setJobCover(images.get(0));
                        }
                    } catch (Exception e) {
                        vo.setJobCover(job.getCoverImages());
                    }
                }
            }

            // 查询商家信息
            UrMerchantProfiles merchantProfile = urMerchantProfilesService.getById(order.getMerchantId());
            if (merchantProfile != null) {
                vo.setCompanyName(merchantProfile.getCompanyName());
                vo.setContactPhone(merchantProfile.getContactPhone());
            }

            // 设置过期时间
            if (job != null) {
                vo.setExpireTime(job.getExpireTime());
            }

            return vo;
        }).collect(java.util.stream.Collectors.toList()));

        return ResultUtils.success(resultPage);
    }

    /**
     * 用户订单详情接口
     */
    @ApiOperation(value = "订单详情", notes = "获取订单详细信息，包含兼职信息和商家信息")
    @GetMapping("/detail/{id}")
    public BaseResponse<Map<String, Object>> getOrderDetail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        OdOrders order = odOrdersService.getById(id);
        if (order == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        if (!order.getUserId().equals(userId)) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权查看该订单");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("jobId", order.getJobId());
        result.put("orderStatus", order.getOrderStatus());
        result.put("orderStatusDesc", getOrderStatusDesc(order.getOrderStatus()));
        result.put("tradeMode", order.getTradeMode());
        result.put("tradeModeDesc", getTradeModeDesc(order.getTradeMode()));
        result.put("createdAt", order.getCreatedAt());
        result.put("startTime", order.getStartTime());
        result.put("endTime", order.getEndTime());
        result.put("completedAt", order.getCompletedAt());
        result.put("penaltyAmount", order.getPenaltyAmount());

        JbJobs job = jbJobsService.getById(order.getJobId());
        if (job != null) {
            Map<String, Object> jobInfo = new HashMap<>();
            jobInfo.put("id", job.getId());
            jobInfo.put("title", job.getTitle());
            jobInfo.put("description", job.getDescription());
            jobInfo.put("salaryMin", job.getSalaryMin());
            jobInfo.put("salaryMax", job.getSalaryMax());
            jobInfo.put("jobType", job.getJobType());
            jobInfo.put("jobTypeDesc", getJobTypeDesc(job.getJobType()));
            jobInfo.put("regionName", job.getRegionName());
            jobInfo.put("depositAmount", job.getDepositAmount());
            jobInfo.put("workTimeDesc", job.getWorkTimeDesc());

            if (job.getCoverImages() != null && !job.getCoverImages().isEmpty()) {
                try {
                    List<String> images = objectMapper.readValue(job.getCoverImages(), new TypeReference<List<String>>() {});
                    jobInfo.put("coverImages", images);
                } catch (Exception e) {
                    jobInfo.put("coverImages", Arrays.asList(job.getCoverImages()));
                }
            }
            result.put("jobInfo", jobInfo);
        }

        UrMerchantProfiles merchant = urMerchantProfilesService.getById(order.getMerchantId());
        if (merchant != null) {
            Map<String, Object> merchantInfo = new HashMap<>();
            merchantInfo.put("id", merchant.getUserId());
            merchantInfo.put("merchantName", merchant.getCompanyName());
            merchantInfo.put("businessLicense", merchant.getLicenseUrl());
            merchantInfo.put("contactPhone", merchant.getContactPhone());
            merchantInfo.put("address", merchant.getCompanyAddress());
            result.put("merchantInfo", merchantInfo);
        }

        return ResultUtils.success(result);
    }

    /**
     * 取消订单接口
     * 删除订单，同时处理商家钱包：冻结金额-押金，余额+押金
     */
    @ApiOperation(value = "取消订单", notes = "取消待入职订单，同时处理商家钱包：冻结金额-押金，余额+押金")
    @PostMapping("/cancel/{id}")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> cancelOrder(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        // 查询订单
        OdOrders order = odOrdersService.getById(id);
        if (order == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作该订单");
        }

        // 验证订单状态是否为待入职（orderStatus === 1）
        if (order.getOrderStatus() == null || order.getOrderStatus() != 1) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "只有待入职的订单可以取消");
        }

        // 查询兼职岗位获取薪资
        JbJobs job = jbJobsService.getById(order.getJobId());
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职岗位不存在");
        }

        BigDecimal salaryAmount = job.getSalaryMin();
        if (salaryAmount == null) {
            salaryAmount = BigDecimal.ZERO;
        }

        // 查询商家钱包
        Long merchantId = order.getMerchantId();
        WlWallets wallet = wlWalletsService.getById(merchantId);
        if (wallet == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "商家钱包不存在");
        }

        // 更新钱包：冻结金额-薪资，余额+薪资
        BigDecimal newFrozenBalance = wallet.getFrozenBalance().subtract(salaryAmount);
        if (newFrozenBalance.compareTo(BigDecimal.ZERO) < 0) {
            newFrozenBalance = BigDecimal.ZERO;
        }
        BigDecimal newBalance = wallet.getBalance().add(salaryAmount);

        wallet.setFrozenBalance(newFrozenBalance);
        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(new Date());

        boolean walletUpdated = wlWalletsService.updateById(wallet);
        if (!walletUpdated) {
            throw new RuntimeException("钱包更新失败");
        }

        // 删除订单
        boolean orderDeleted = odOrdersService.removeById(id);
        if (!orderDeleted) {
            throw new RuntimeException("订单删除失败");
        }

        return ResultUtils.success("取消订单成功");
    }

    /**
     * 用户同意兼职并支付押金
     * 用户点击同意入职后，冻结用户钱包中的押金金额，订单状态变为2（进行中）
     */
    @ApiOperation(value = "用户同意兼职", notes = "用户同意入职，冻结押金，订单状态变为进行中")
    @PostMapping("/confirm")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> confirmOrder(@RequestBody Map<String, Long> request, HttpServletRequest httpRequest) {
        Long orderId = request.get("orderId");
        if (orderId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
        }

        Long userId = getUserIdFromCookie(httpRequest);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        // 查询订单
        OdOrders order = odOrdersService.getById(orderId);
        if (order == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 验证订单属于当前用户
        if (!order.getUserId().equals(userId)) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作该订单");
        }

        // 验证订单状态为待入职(1)
        if (order.getOrderStatus() != 1) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单状态不正确，当前状态：" + getOrderStatusDesc(order.getOrderStatus()));
        }

        // 查询兼职岗位获取押金金额
        JbJobs job = jbJobsService.getById(order.getJobId());
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职岗位不存在");
        }

        BigDecimal depositAmount = job.getDepositAmount();
        if (depositAmount == null || depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "押金金额无效");
        }

        // 查询用户钱包
        WlWallets wallet = wlWalletsService.getById(userId);
        if (wallet == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "用户钱包不存在");
        }

        // 验证余额是否充足
        if (wallet.getBalance().compareTo(depositAmount) < 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "余额不足，当前余额：" + wallet.getBalance() + "，需冻结押金：" + depositAmount);
        }

        // 更新钱包：余额-押金，冻结金额+押金
        BigDecimal newBalance = wallet.getBalance().subtract(depositAmount);
        BigDecimal newFrozenBalance = wallet.getFrozenBalance().add(depositAmount);

        wallet.setBalance(newBalance);
        wallet.setFrozenBalance(newFrozenBalance);
        wallet.setUpdatedAt(new Date());

        boolean walletUpdated = wlWalletsService.updateById(wallet);
        if (!walletUpdated) {
            throw new RuntimeException("钱包更新失败");
        }

        // 更新订单状态为进行中(2)
        order.setOrderStatus(2);
        order.setStartTime(new Date());

        boolean orderUpdated = odOrdersService.updateById(order);
        if (!orderUpdated) {
            throw new RuntimeException("订单更新失败");
        }

        return ResultUtils.success("已同意入职，押金已冻结");
    }
}
