package com.springbootinit.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.OdOrders;
import com.springbootinit.model.domain.UrIntentions;
import com.springbootinit.model.domain.UrMerchantProfiles;
import com.springbootinit.model.domain.WlFreezeRecords;
import com.springbootinit.model.dto.OrderSearchWrapper;
import com.springbootinit.model.vo.AdminOrderVO;
import com.springbootinit.service.OdOrdersService;
import com.springbootinit.service.UrIntentionsService;
import com.springbootinit.service.UrMerchantProfilesService;
import com.springbootinit.service.WlFreezeRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员-订单管理接口
 */
@Api(tags = "管理员-订单管理")
@RestController
@RequestMapping("/admin/yydd")
public class AdminOrderController {

    @Autowired
    private OdOrdersService odOrdersService;

    @Autowired
    private WlFreezeRecordsService wlFreezeRecordsService;

    @Autowired
    private UrIntentionsService urIntentionsService;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    /**
     * 获取订单列表（分页）
     *
     * @param searchWrapper 搜索包装参数
     * @return 订单列表
     */
    @ApiOperation(value = "获取订单列表", notes = "分页获取订单列表，支持按多个字段搜索")
    @PostMapping("/list")
    public BaseResponse<Page<AdminOrderVO>> getOrders(
            @ApiParam(value = "搜索包装参数") @RequestBody OrderSearchWrapper searchWrapper) {
        Page<OdOrders> pageInfo = new Page<>(searchWrapper.getPage().getPageNum(), searchWrapper.getPage().getPageSize());
        QueryWrapper<OdOrders> queryWrapper = new QueryWrapper<>();
        
        if (searchWrapper.getParams().getOrderId() != null) {
            queryWrapper.eq("id", searchWrapper.getParams().getOrderId());
        }
        if (searchWrapper.getParams().getUserId() != null) {
            queryWrapper.eq("user_id", searchWrapper.getParams().getUserId());
        }
        if (searchWrapper.getParams().getMerchantId() != null) {
            queryWrapper.eq("merchant_id", searchWrapper.getParams().getMerchantId());
        }
        if (searchWrapper.getParams().getJobId() != null) {
            queryWrapper.eq("job_id", searchWrapper.getParams().getJobId());
        }
        if (searchWrapper.getParams().getOrderStatus() != null) {
            queryWrapper.eq("order_status", searchWrapper.getParams().getOrderStatus());
        }
        if (searchWrapper.getParams().getTradeMode() != null) {
            queryWrapper.eq("trade_mode", searchWrapper.getParams().getTradeMode());
        }
        
        queryWrapper.orderByDesc("created_at");
        Page<OdOrders> orderPage = odOrdersService.page(pageInfo, queryWrapper);
        
        List<AdminOrderVO> voList = new ArrayList<>();
        for (OdOrders order : orderPage.getRecords()) {
            AdminOrderVO vo = AdminOrderVO.fromOrder(order);
            
            QueryWrapper<UrIntentions> intentionQuery = new QueryWrapper<>();
            intentionQuery.eq("user_id", order.getUserId());
            UrIntentions intention = urIntentionsService.getOne(intentionQuery);
            if (intention != null) {
                vo.setStudentId(intention.getStudentId());
                vo.setRealName(intention.getRealName());
                vo.setAge(intention.getAge());
                vo.setGender(intention.getGender());
                vo.setPhone(intention.getPhone());
                vo.setProfession(intention.getProfession());
                vo.setIntroduction(intention.getIntroduction());
                vo.setTagName(intention.getTagName());
                vo.setCreditScore(intention.getCreditScore());
            }
            
            QueryWrapper<UrMerchantProfiles> merchantQuery = new QueryWrapper<>();
            merchantQuery.eq("user_id", order.getMerchantId());
            UrMerchantProfiles merchant = urMerchantProfilesService.getOne(merchantQuery);
            if (merchant != null) {
                vo.setCompanyName(merchant.getCompanyName());
                vo.setLicenseUrl(merchant.getLicenseUrl());
                vo.setContactPhone(merchant.getContactPhone());
                vo.setLocation(merchant.getLocation());
                vo.setLegalPerson(merchant.getLegalPerson());
                vo.setRegisteredCapital(merchant.getRegisteredCapital());
                vo.setCompanyAddress(merchant.getCompanyAddress());
                vo.setCompanyIntro(merchant.getCompanyIntro());
            }
            
            voList.add(vo);
        }
        
        Page<AdminOrderVO> resultPage = new Page<>();
        resultPage.setRecords(voList);
        resultPage.setSize(searchWrapper.getPage().getPageSize());
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setCurrent(searchWrapper.getPage().getPageNum());
        
        return ResultUtils.success(resultPage);
    }

    /**
     * 获取订单详情
     *
     * @param request 请求参数
     * @return 订单详情
     */
    @ApiOperation(value = "获取订单详情", notes = "根据ID获取订单详细信息")
    @PostMapping("/detail")
    public BaseResponse<OdOrders> getOrder(@ApiParam(value = "请求参数", required = true) @RequestBody DetailRequest request) {
        OdOrders order = odOrdersService.getById(request.getId());
        if (order == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单不存在");
        }
        return ResultUtils.success(order);
    }

    // 详情请求参数类
    private static class DetailRequest {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    /**
     * 更新订单
     *
     * @param order 订单信息
     * @return 更新结果
     */
    @ApiOperation(value = "更新订单", notes = "更新订单信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateOrder(@ApiParam(value = "订单信息", required = true) @RequestBody OdOrders order) {
        if (order.getId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
        }
        OdOrders existing = odOrdersService.getById(order.getId());
        if (existing == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单不存在");
        }

        boolean update = odOrdersService.updateById(order);
        return ResultUtils.success(update);
    }

    /**
     * 删除订单
     *
     * @param request 请求参数
     * @return 删除结果
     */
    @ApiOperation(value = "删除订单", notes = "删除订单")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrder(@ApiParam(value = "请求参数", required = true) @RequestBody DetailRequest request) {
        OdOrders existing = odOrdersService.getById(request.getId());
        if (existing == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单不存在");
        }
        boolean delete = odOrdersService.removeById(request.getId());
        return ResultUtils.success(delete);
    }

    /**
     * 导出订单列表到Excel
     */
    @ApiOperation(value = "导出订单列表", notes = "将订单列表导出为Excel文件")
    @PostMapping("/export")
    public void exportOrders(HttpServletResponse response) throws Exception {
        List<OdOrders> orders = odOrdersService.list();

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("订单列表", "UTF-8") + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 使用EasyExcel导出
        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), OdOrders.class)
                .sheet("订单列表")
                .doWrite(orders);
    }

    @ApiOperation(value = "退押金", notes = "退还订单押金")
    @PostMapping("/refundDeposit")
    public BaseResponse<Boolean> refundDeposit(@ApiParam(value = "请求参数", required = true) @RequestBody DetailRequest request) {
        OdOrders order = odOrdersService.getById(request.getId());
        if (order == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单不存在");
        }
        
        if (order.getOrderStatus() != 2 && order.getOrderStatus() != 3) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单状态不允许退押金");
        }
        
        if (order.getDepositFrozenId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该订单没有押金冻结记录");
        }
        
        WlFreezeRecords freezeRecord = wlFreezeRecordsService.getById(order.getDepositFrozenId());
        if (freezeRecord == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "押金冻结记录不存在");
        }
        
        if (freezeRecord.getStatus() != 1) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "押金已处理，无法重复操作");
        }
        
        WlFreezeRecords updateRecord = new WlFreezeRecords();
        updateRecord.setId(freezeRecord.getId());
        updateRecord.setStatus(2);
        updateRecord.setReleaseReason("管理员操作退款");
        boolean updateFreeze = wlFreezeRecordsService.updateById(updateRecord);
        
        if (updateFreeze) {
            OdOrders updateOrder = new OdOrders();
            updateOrder.setId(order.getId());
            updateOrder.setOrderStatus(5);
            odOrdersService.updateById(updateOrder);
        }
        
        return ResultUtils.success(updateFreeze);
    }

    @ApiOperation(value = "订单结款", notes = "完成订单结款")
    @PostMapping("/settleOrder")
    public BaseResponse<Boolean> settleOrder(@ApiParam(value = "请求参数", required = true) @RequestBody DetailRequest request) {
        OdOrders order = odOrdersService.getById(request.getId());
        if (order == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单不存在");
        }
        
        if (order.getOrderStatus() != 3) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "只有已完成状态的订单才能结款");
        }
        
        OdOrders updateOrder = new OdOrders();
        updateOrder.setId(order.getId());
        updateOrder.setOrderStatus(5);
        boolean update = odOrdersService.updateById(updateOrder);
        
        return ResultUtils.success(update);
    }

    @ApiOperation(value = "订单退款", notes = "订单退款给用户")
    @PostMapping("/refundOrder")
    public BaseResponse<Boolean> refundOrder(@ApiParam(value = "请求参数", required = true) @RequestBody DetailRequest request) {
        OdOrders order = odOrdersService.getById(request.getId());
        if (order == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单不存在");
        }
        
        if (order.getOrderStatus() != 2 && order.getOrderStatus() != 3 && order.getOrderStatus() != 4) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "订单状态不允许退款");
        }
        
        if (order.getDepositFrozenId() != null) {
            WlFreezeRecords freezeRecord = wlFreezeRecordsService.getById(order.getDepositFrozenId());
            if (freezeRecord != null && freezeRecord.getStatus() == 1) {
                WlFreezeRecords updateRecord = new WlFreezeRecords();
                updateRecord.setId(freezeRecord.getId());
                updateRecord.setStatus(2);
                updateRecord.setReleaseReason("管理员操作订单退款");
                wlFreezeRecordsService.updateById(updateRecord);
            }
        }
        
        OdOrders updateOrder = new OdOrders();
        updateOrder.setId(order.getId());
        updateOrder.setOrderStatus(7);
        boolean update = odOrdersService.updateById(updateOrder);
        
        return ResultUtils.success(update);
    }

}
