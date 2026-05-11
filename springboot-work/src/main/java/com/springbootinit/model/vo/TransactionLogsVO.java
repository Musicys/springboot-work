package com.springbootinit.model.vo;

import com.springbootinit.model.domain.WlTransactionLogs;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionLogsVO {

    private Long id;
    private Long userId;
    private Long operatorId;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private Long relatedOrderId;
    private Long relatedFreezeId;
    private String description;
    private Date createdAt;

    private String realName;
    private String phone;
    private String studentId;
    private String profession;
    private String tagName;

    public static TransactionLogsVO fromTransaction(WlTransactionLogs transaction) {
        TransactionLogsVO vo = new TransactionLogsVO();
        vo.setId(transaction.getId());
        vo.setUserId(transaction.getUserId());
        vo.setOperatorId(transaction.getOperatorId());
        vo.setType(transaction.getType());
        vo.setAmount(transaction.getAmount());
        vo.setBalanceBefore(transaction.getBalanceBefore());
        vo.setBalanceAfter(transaction.getBalanceAfter());
        vo.setRelatedOrderId(transaction.getRelatedOrderId());
        vo.setRelatedFreezeId(transaction.getRelatedFreezeId());
        vo.setDescription(transaction.getDescription());
        vo.setCreatedAt(transaction.getCreatedAt());
        return vo;
    }
}
