package com.springbootinit.controller.admin;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仲裁案件接口
 */
@Api(tags = "仲裁案件接口")
@RestController
@RequestMapping("/admin/arbitration")
public class DsArbitrationCasesController {

    @Autowired
    private DsArbitrationCasesService dsArbitrationCasesService;

    @Autowired
    private UrUsersService urUsersService;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取仲裁案件列表
     */
    @ApiOperation(value = "获取仲裁案件列表", notes = "分页查询仲裁案件，联查用户和商家信息")
    @GetMapping("/list")
    public BaseResponse<Page<ArbitrationCaseVO>> getCaseList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Integer status) {

        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        Page<DsArbitrationCases> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DsArbitrationCases> queryWrapper = new QueryWrapper<>();

        if (id != null) {
            queryWrapper.eq("id", id);
        }
        if (orderId != null) {
            queryWrapper.eq("order_id", orderId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        queryWrapper.orderByDesc("created_at");

        Page<DsArbitrationCases> casePage = dsArbitrationCasesService.page(page, queryWrapper);

        Page<ArbitrationCaseVO> resultPage = new Page<>(casePage.getCurrent(), casePage.getSize(), casePage.getTotal());
        List<ArbitrationCaseVO> voList = new ArrayList<>();
        for (DsArbitrationCases caseInfo : casePage.getRecords()) {
            voList.add(convertToVO(caseInfo));
        }
        resultPage.setRecords(voList);

        return ResultUtils.success(resultPage);
    }

    /**
     * 获取仲裁案件详情
     */
    @ApiOperation(value = "获取仲裁案件详情", notes = "根据ID查询仲裁案件详情，包含用户和商家信息")
    @GetMapping("/detail/{id}")
    public BaseResponse<ArbitrationCaseVO> getCaseDetail(@PathVariable Long id) {
        DsArbitrationCases caseInfo = dsArbitrationCasesService.getById(id);
        if (caseInfo == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "案件不存在");
        }

        ArbitrationCaseVO vo = convertToVO(caseInfo);
        return ResultUtils.success(vo);
    }

    /**
     * 更新仲裁案件状态（结案）
     */
    @ApiOperation(value = "结案处理", notes = "管理员处理仲裁案件，更新状态和裁决结果")
    @PostMapping("/close/{id}")
    public BaseResponse<Boolean> closeCase(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        DsArbitrationCases caseInfo = dsArbitrationCasesService.getById(id);
        if (caseInfo == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "案件不存在");
        }

        if (caseInfo.getStatus() != null && caseInfo.getStatus() == 2) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "案件已结案");
        }

        // 更新裁决结果
        if (requestBody.containsKey("rulingResult")) {
            caseInfo.setRulingResult(Integer.parseInt(requestBody.get("rulingResult").toString()));
        }
        if (requestBody.containsKey("faultParty")) {
            caseInfo.setFaultParty(Byte.parseByte(requestBody.get("faultParty").toString()));
        }
        if (requestBody.containsKey("deductedScore")) {
            caseInfo.setDeductedScore(Integer.parseInt(requestBody.get("deductedScore").toString()));
        }
        if (requestBody.containsKey("adminComment")) {
            caseInfo.setAdminComment(requestBody.get("adminComment").toString());
        }
        if (requestBody.containsKey("finalSettlementAmount")) {
            caseInfo.setFinalSettlementAmount(new java.math.BigDecimal(requestBody.get("finalSettlementAmount").toString()));
        }

        caseInfo.setStatus(2); // 已结案
        caseInfo.setClosedAt(new java.util.Date());

        boolean updated = dsArbitrationCasesService.updateById(caseInfo);
        if (!updated) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "结案失败");
        }

        return ResultUtils.success(true);
    }

    /**
     * 转换为VO对象
     */
    private ArbitrationCaseVO convertToVO(DsArbitrationCases caseInfo) {
        ArbitrationCaseVO vo = new ArbitrationCaseVO();

        // 设置案件基本信息
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

        // 解析用户证据JSON
        parseUserEvidence(caseInfo.getUserEvidenceJson(), vo);

        // 解析商户证据JSON
        parseMerchantEvidence(caseInfo.getMerchantEvidenceJson(), vo);

        // 查询用户信息
        if (caseInfo.getUserId() != null) {
            UrUsers user = urUsersService.getById(caseInfo.getUserId());
            if (user != null) {
                vo.setUserUsername(user.getUsername());
                vo.setUserAvatarUrl(user.getAvatarUrl());
                vo.setUserCreditCode(user.getCreditScore());
            }
        }

        // 查询商家信息（通过shop_id关联）
        if (caseInfo.getShopId() != null) {
            UrMerchantProfiles merchant = urMerchantProfilesService.getById(caseInfo.getShopId());
            if (merchant != null) {
                vo.setCompanyName(merchant.getCompanyName());
                vo.setContactPhone(merchant.getContactPhone());
                vo.setCompanyAddress(merchant.getCompanyAddress());
            }
        }

        return vo;
    }

    /**
     * 解析用户证据JSON
     */
    private void parseUserEvidence(JsonNode jsonNode, ArbitrationCaseVO vo) {
        if (jsonNode == null) {
            return;
        }
        try {
            Map<String, Object> evidence = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {});
            if (evidence.containsKey("images")) {
                vo.setUserEvidenceImages((List<String>) evidence.get("images"));
            }
            if (evidence.containsKey("description")) {
                vo.setUserEvidenceDescription((String) evidence.get("description"));
            }
            if (evidence.containsKey("invitation")) {
                vo.setUserEvidenceInvitation((String) evidence.get("invitation"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析商户证据JSON
     */
    private void parseMerchantEvidence(JsonNode jsonNode, ArbitrationCaseVO vo) {
        if (jsonNode == null) {
            return;
        }
        try {
            Map<String, Object> evidence = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {});
            if (evidence.containsKey("images")) {
                vo.setMerchantEvidenceImages((List<String>) evidence.get("images"));
            }
            if (evidence.containsKey("description")) {
                vo.setMerchantEvidenceDescription((String) evidence.get("description"));
            }
            if (evidence.containsKey("invitation")) {
                vo.setMerchantEvidenceInvitation((String) evidence.get("invitation"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
