package com.springbootinit.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ArbitrationCaseVO {
    // 仲裁案件信息
    private Long id;
    private Long orderId;
    private Long shopId;
    private Long userId;
    
    private String userEvidenceSummary;
    private String merchantEvidenceSummary;
    private String adminComment;
    private Integer rulingResult;
    private String rulingResultDesc;
    private BigDecimal finalSettlementAmount;
    private Long handledByAdminId;
    private Integer faultParty;
    private String faultPartyDesc;
    private Integer deductedScore;
    private Integer status;
    private String statusDesc;
    private Date createdAt;
    private Date closedAt;
    
    // 用户证据JSON字段
    private List<String> userEvidenceImages;
    private String userEvidenceDescription;
    private String userEvidenceInvitation;
    
    // 商户证据JSON字段
    private List<String> merchantEvidenceImages;
    private String merchantEvidenceDescription;
    private String merchantEvidenceInvitation;
    
    // 用户信息
    private String userUsername;
    private String userAvatarUrl;
    private Integer userCreditCode;
    
    // 商家信息
    private String companyName;
    private String contactPhone;
    private String companyAddress;
    
    // 获取状态描述
    public static String getStatusDesc(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "审理中";
            case 2: return "已结案";
            default: return "未知";
        }
    }
    
    // 获取裁决结果描述
    public static String getRulingResultDesc(Integer result) {
        if (result == null) return "未知";
        switch (result) {
            case 1: return "全额结款";
            case 2: return "部分结款";
            case 3: return "不结款";
            case 4: return "退还押金";
            default: return "未知";
        }
    }
    
    // 获取出错方描述
    public static String getFaultPartyDesc(Integer party) {
        if (party == null) return "未知";
        switch (party) {
            case 0: return "用户";
            case 1: return "商家";
            case 3: return "无错/无法判定";
            default: return "未知";
        }
    }
}