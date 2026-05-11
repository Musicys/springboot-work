package com.springbootinit.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.*;
import com.springbootinit.model.vo.ArbitrationCaseVO;
import com.springbootinit.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仲裁案件接口
 */
@Api(tags = "用户仲裁案件接口")
@RestController
@RequestMapping("/user/arbitration")
public class UserDsArbitrationCasesController {

    @Autowired
    private DsArbitrationCasesService dsArbitrationCasesService;

    @Autowired
    private UrUsersService urUsersService;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    @Autowired
    private OdOrdersService odOrdersService;

    @Autowired
    private JbJobsService jbJobsService;

    @Autowired(required = false)
    private com.springbootinit.utils.JwtUtils jwtUtils;

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

    /**
     * 用户提交异议申请
     */
    @ApiOperation(value = "提交异议申请", notes = "用户提交异议申请，创建仲裁案件并将订单状态改为纠纷中")
    @PostMapping("/apply")
    public BaseResponse<Boolean> applyArbitration(
            javax.servlet.http.HttpServletRequest request,
            @RequestBody Map<String, Object> requestBody) {

        // 获取用户ID（从Cookie中获取）
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        // 获取请求参数
        Long orderId = requestBody.get("orderId") != null ?
            Long.valueOf(requestBody.get("orderId").toString()) : null;
        String userEvidenceSummary = requestBody.get("userEvidenceSummary") != null ?
            requestBody.get("userEvidenceSummary").toString() : null;
        List<String> userEvidenceImages = (List<String>) requestBody.get("userEvidenceImages");

        if (orderId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
        }
        if (userEvidenceSummary == null || userEvidenceSummary.trim().isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户证词不能为空");
        }
        if (userEvidenceImages == null || userEvidenceImages.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请上传证据图片");
        }

        // 查询订单信息
        OdOrders order = odOrdersService.getById(orderId);
        if (order == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 验证订单所属用户
        if (!order.getUserId().equals(userId)) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权操作该订单");
        }

        // 检查订单状态是否允许申请异议（状态为2：进行中 或 状态为3：完成待结算）
        if (order.getOrderStatus() != 2 && order.getOrderStatus() != 3) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该订单状态不允许申请异议");
        }

        // 检查是否已经有仲裁案件
        QueryWrapper<DsArbitrationCases> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        DsArbitrationCases existingCase = dsArbitrationCasesService.getOne(queryWrapper);
        if (existingCase != null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该订单已存在仲裁案件");
        }

        // 查询兼职信息获取商家ID
        JbJobs job = jbJobsService.getById(order.getJobId());
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职岗位不存在");
        }

        // 创建用户证据JSON
        Map<String, Object> userEvidenceJson = new HashMap<>();
        userEvidenceJson.put("images", userEvidenceImages);
        userEvidenceJson.put("description", userEvidenceSummary);
        userEvidenceJson.put("invitation", "邀请您在24小时内审核此证据，并确认是否通过。");

        // 创建仲裁案件
        DsArbitrationCases arbitrationCase = new DsArbitrationCases();
        arbitrationCase.setOrderId(orderId);
        arbitrationCase.setShopId(job.getMerchantId());
        arbitrationCase.setUserId(userId);
        arbitrationCase.setUserEvidenceSummary(userEvidenceSummary);
        try {
            String userEvidenceJsonStr = objectMapper.writeValueAsString(userEvidenceJson);
            arbitrationCase.setUserEvidenceJson(objectMapper.readTree(userEvidenceJsonStr));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "证据JSON序列化失败");
        }
        arbitrationCase.setStatus(1); // 1:审理中

        boolean saved = dsArbitrationCasesService.save(arbitrationCase);
        if (!saved) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "创建仲裁案件失败");
        }

        // 更新订单状态为7（纠纷中）
        order.setOrderStatus(7);
        boolean orderUpdated = odOrdersService.updateById(order);
        if (!orderUpdated) {
            // 回滚仲裁案件
            dsArbitrationCasesService.removeById(arbitrationCase.getId());
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "更新订单状态失败");
        }

        return ResultUtils.success(true);
    }

    /**
     * 获取用户的仲裁案件列表
     */
    @ApiOperation(value = "获取用户仲裁案件列表", notes = "获取当前用户的仲裁案件列表")
    @GetMapping("/list")
    public BaseResponse<List<ArbitrationCaseVO>> getUserCases(javax.servlet.http.HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        QueryWrapper<DsArbitrationCases> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("created_at");

        List<DsArbitrationCases> cases = dsArbitrationCasesService.list(queryWrapper);

        List<ArbitrationCaseVO> voList = new ArrayList<>();
        for (DsArbitrationCases caseInfo : cases) {
            voList.add(convertToVO(caseInfo));
        }

        return ResultUtils.success(voList);
    }

    /**
     * 获取仲裁案件详情
     */
    @ApiOperation(value = "获取仲裁案件详情", notes = "获取仲裁案件详情")
    @GetMapping("/detail/{id}")
    public BaseResponse<ArbitrationCaseVO> getCaseDetail(
            javax.servlet.http.HttpServletRequest request,
            @PathVariable Long id) {

        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        DsArbitrationCases caseInfo = dsArbitrationCasesService.getById(id);
        if (caseInfo == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "案件不存在");
        }

        // 验证用户权限
        if (!caseInfo.getUserId().equals(userId)) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "无权查看该案件");
        }

        ArbitrationCaseVO vo = convertToVO(caseInfo);
        return ResultUtils.success(vo);
    }

    /**
     * 转换为VO对象
     */
    private ArbitrationCaseVO convertToVO(DsArbitrationCases caseInfo) {
        ArbitrationCaseVO vo = new ArbitrationCaseVO();

        vo.setId(caseInfo.getId());
        vo.setOrderId(caseInfo.getOrderId());
        vo.setShopId(caseInfo.getShopId());
        vo.setUserId(caseInfo.getUserId());
        vo.setUserEvidenceSummary(caseInfo.getUserEvidenceSummary());
        vo.setMerchantEvidenceSummary(caseInfo.getMerchantEvidenceSummary());
        vo.setAdminComment(caseInfo.getAdminComment());
        vo.setRulingResult(caseInfo.getRulingResult());
        vo.setRulingResultDesc(ArbitrationCaseVO.getRulingResultDesc(caseInfo.getRulingResult()));
        vo.setFinalSettlementAmount(caseInfo.getFinalSettlementAmount());
        vo.setHandledByAdminId(caseInfo.getHandledByAdminId());
        vo.setFaultParty(caseInfo.getFaultParty() != null ? caseInfo.getFaultParty().intValue() : null);
        vo.setFaultPartyDesc(ArbitrationCaseVO.getFaultPartyDesc(vo.getFaultParty()));
        vo.setDeductedScore(caseInfo.getDeductedScore());
        vo.setStatus(caseInfo.getStatus());
        vo.setStatusDesc(ArbitrationCaseVO.getStatusDesc(caseInfo.getStatus()));
        vo.setCreatedAt(caseInfo.getCreatedAt());
        vo.setClosedAt(caseInfo.getClosedAt());

        // 查询商家信息
        if (caseInfo.getShopId() != null) {
            UrMerchantProfiles merchant = urMerchantProfilesService.getById(caseInfo.getShopId());
            if (merchant != null) {
                vo.setCompanyName(merchant.getCompanyName());
                vo.setContactPhone(merchant.getContactPhone());
            }
        }

        return vo;
    }
}
