package com.springbootinit.controller.pro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.UrIntentions;
import com.springbootinit.model.domain.WlTransactionLogs;
import com.springbootinit.model.dto.TransactionLogsQueryDTO;
import com.springbootinit.model.vo.TransactionLogsVO;
import com.springbootinit.service.UrIntentionsService;
import com.springbootinit.service.WlTransactionLogsService;
import com.springbootinit.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = "交易流水接口")
@RestController
@RequestMapping("/pro/transaction")
public class WlTransactionLogsController {

    @Autowired
    private WlTransactionLogsService wlTransactionLogsService;

    @Autowired
    private UrIntentionsService urIntentionsService;

    @Autowired
    private JwtUtils jwtUtils;

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

    @ApiOperation(value = "获取交易流水列表", notes = "获取当前商家的交易流水列表，支持分页和关键词搜索")
    @PostMapping("/list")
    public BaseResponse<Page<TransactionLogsVO>> getTransactionList(
            @RequestBody TransactionLogsQueryDTO queryDTO,
            HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        Page<WlTransactionLogs> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        QueryWrapper<WlTransactionLogs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_id", userId);

        if (queryDTO.getUserId() != null) {
            queryWrapper.eq("user_id", queryDTO.getUserId());
        }

        if (StringUtils.isNotBlank(queryDTO.getType())) {
            queryWrapper.eq("type", queryDTO.getType());
        }

        if (queryDTO.getRelatedOrderId() != null) {
            queryWrapper.eq("related_order_id", queryDTO.getRelatedOrderId());
        }

        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            queryWrapper.and(w -> w.like("description", queryDTO.getKeyword())
                    .or().eq("related_order_id", queryDTO.getKeyword()));
        }

        queryWrapper.orderByDesc("created_at");
        Page<WlTransactionLogs> transactionPage = wlTransactionLogsService.page(page, queryWrapper);

        List<TransactionLogsVO> voList = new ArrayList<>();
        for (WlTransactionLogs transaction : transactionPage.getRecords()) {
            TransactionLogsVO vo = TransactionLogsVO.fromTransaction(transaction);

            QueryWrapper<UrIntentions> intentionQuery = new QueryWrapper<>();
            intentionQuery.eq("user_id", transaction.getUserId());
            UrIntentions intention = urIntentionsService.getOne(intentionQuery);
            if (intention != null) {
                vo.setRealName(intention.getRealName());
                vo.setPhone(intention.getPhone());
                vo.setStudentId(intention.getStudentId());
                vo.setProfession(intention.getProfession());
                vo.setTagName(intention.getTagName());
            }

            voList.add(vo);
        }

        Page<TransactionLogsVO> resultPage = new Page<>();
        resultPage.setRecords(voList);
        resultPage.setSize(transactionPage.getSize());
        resultPage.setTotal(transactionPage.getTotal());
        resultPage.setCurrent(transactionPage.getCurrent());

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "获取交易流水详情", notes = "获取交易流水详细信息")
    @GetMapping("/get/{id}")
    public BaseResponse<WlTransactionLogs> getTransactionDetail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        WlTransactionLogs transaction = wlTransactionLogsService.getById(id);
        if (transaction == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "交易记录不存在");
        }

        if (!transaction.getUserId().equals(userId)) {
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权查看该记录");
        }

        return ResultUtils.success(transaction);
    }
}
