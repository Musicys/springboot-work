package com.springbootinit.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.JbJobs;
import com.springbootinit.model.domain.OdOrders;
import com.springbootinit.model.domain.UrMerchantProfiles;
import com.springbootinit.model.vo.JobDetailVO;
import com.springbootinit.model.vo.JobVO;
import com.springbootinit.model.vo.OrderDetailVO;
import com.springbootinit.model.vo.OrderVO;
import com.springbootinit.service.JbJobsService;
import com.springbootinit.service.OdOrdersService;
import com.springbootinit.service.UrMerchantProfilesService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(tags = "兼职岗位接口")
@RestController
@RequestMapping("/user/job")
public class JobController {

    @Autowired
    private JbJobsService jbJobsService;

    @Autowired
    private OdOrdersService odOrdersService;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    private String getWorkTimeDesc(Integer workTimeType) {
        if (workTimeType == null) return "未知";
        switch (workTimeType) {
            case 1: return "周末";
            case 2: return "周一至周五";
            case 3: return "自定义时间";
            default: return "未知";
        }
    }

    private String getSettlementCycleDesc(Integer cycle) {
        if (cycle == null) return "未知";
        switch (cycle) {
            case 1: return "日结";
            case 2: return "周结";
            case 3: return "月结";
            case 4: return "完工结";
            default: return "未知";
        }
    }

    private String getOrderStatusDesc(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "待入职";
            case 2: return "进行中";
            case 3: return "完成待结算";
            case 4: return "纠纷中";
            case 5: return "已结款";
            case 6: return "用户爽约";
            case 7: return "异常终止";
            default: return "未知";
        }
    }

    private String getTradeModeDesc(Integer mode) {
        if (mode == null) return "未知";
        return mode == 1 ? "平台担保" : "线下交易";
    }

    @ApiOperation(value = "兼职列表", notes = "获取兼职岗位列表，支持分页和筛选")
    @GetMapping("/list")
    public BaseResponse<Page<JobVO>> getJobList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer jobType,
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) Integer workTimeType,
            @RequestParam(required = false) String keyword) {

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        Page<JbJobs> page = new Page<>(pageNum, pageSize);
        QueryWrapper<JbJobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        if (jobType != null) {
            queryWrapper.eq("job_type", jobType);
        }
        if (regionName != null && !regionName.isEmpty()) {
            queryWrapper.like("region_name", regionName);
        }
        if (workTimeType != null) {
            queryWrapper.eq("work_time_type", workTimeType);
        }
        // 搜索关键词：匹配标题或描述
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("title", keyword).or().like("description", keyword));
        }
        queryWrapper.orderByDesc("publish_time");

        Page<JbJobs> jobPage = jbJobsService.page(page, queryWrapper);

        // 手动构建分页结果，确保分页信息正确传递
        Page<JobVO> resultPage = new Page<>(jobPage.getCurrent(), jobPage.getSize(), jobPage.getTotal());
        resultPage.setRecords(jobPage.getRecords().stream().map(job -> {
            JobVO vo = new JobVO();
            vo.setId(job.getId());
            vo.setTitle(job.getTitle());
            vo.setSalaryMin(job.getSalaryMin());
            vo.setSalaryMax(job.getSalaryMax());
            vo.setJobType(job.getJobType());
            vo.setJobTypeDesc(getJobTypeDesc(job.getJobType()));
            vo.setRegionName(job.getRegionName());
            vo.setWorkTimeType(job.getWorkTimeType());
            vo.setWorkTimeDesc(job.getWorkTimeDesc());
            vo.setBriefIntro(job.getBriefIntro());
            vo.setRecruitNum(job.getRecruitNum());
            vo.setExpireTime(job.getExpireTime());
            vo.setPublishTime(job.getPublishTime());
            vo.setStatus(job.getStatus());

            if (job.getCoverImages() != null && !job.getCoverImages().isEmpty()) {
                try {
                    List<String> images = objectMapper.readValue(job.getCoverImages(), new TypeReference<List<String>>() {});
                    if (!images.isEmpty()) {
                        vo.setCoverImage(images.get(0));
                    }
                } catch (Exception e) {
                    vo.setCoverImage(job.getCoverImages());
                }
            }

            return vo;
        }).collect(java.util.stream.Collectors.toList()));

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "兼职详情", notes = "获取兼职岗位详细信息")
    @GetMapping("/detail/{id}")
    public BaseResponse<JobDetailVO> getJobDetail(@PathVariable Long id) {
        JbJobs job = jbJobsService.getById(id);
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "兼职岗位不存在");
        }

        JobDetailVO detailVO = new JobDetailVO();
        detailVO.setId(job.getId());
        detailVO.setSalaryMin(job.getSalaryMin());
        detailVO.setSalaryMax(job.getSalaryMax());
        detailVO.setJobType(job.getJobType());
        detailVO.setJobTypeDesc(getJobTypeDesc(job.getJobType()));
        detailVO.setRegionName(job.getRegionName());
        detailVO.setRecruitNum(job.getRecruitNum());
        detailVO.setPublishTime(job.getPublishTime());
        detailVO.setDescription(job.getDescription());
        detailVO.setJobDescCover(job.getJobDescCover());
        detailVO.setJobDetail(job.getJobDetail());
        detailVO.setSettlementCycle(job.getSettlementCycle());
        detailVO.setSettlementCycleDesc(getSettlementCycleDesc(job.getSettlementCycle()));

        if (job.getExpireTime() != null) {
            long diff = job.getExpireTime().getTime() - new Date().getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            detailVO.setRemainingValidDays(Math.max(0L, days));
        }

        if (job.getCoverImages() != null && !job.getCoverImages().isEmpty()) {
            try {
                List<String> images = objectMapper.readValue(job.getCoverImages(), new TypeReference<List<String>>() {});
                detailVO.setCoverImages(images);
            } catch (Exception e) {
                detailVO.setCoverImages(Arrays.asList(job.getCoverImages()));
            }
        }

        UrMerchantProfiles merchant = urMerchantProfilesService.getById(job.getMerchantId());
        if (merchant != null) {
            JobDetailVO.MerchantInfo merchantInfo = new JobDetailVO.MerchantInfo();
            merchantInfo.setId(merchant.getUserId());
            merchantInfo.setMerchantName(merchant.getCompanyName());
            merchantInfo.setBusinessLicense(merchant.getLicenseUrl());
            merchantInfo.setContactPhone(merchant.getContactPhone());
            merchantInfo.setAddress(merchant.getCompanyAddress());
            detailVO.setMerchantInfo(merchantInfo);
        }

        return ResultUtils.success(detailVO);
    }

    @ApiOperation(value = "订单列表", notes = "获取用户订单列表，支持分页")
    @GetMapping("/order/list")
    public BaseResponse<Page<OrderVO>> getOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer orderStatus) {

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        Page<OdOrders> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OdOrders> queryWrapper = new QueryWrapper<>();
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

            JbJobs job = jbJobsService.getById(order.getJobId());
            if (job != null) {
                vo.setJobTitle(job.getTitle());
                vo.setSalary(job.getSalaryMax() != null ? job.getSalaryMax() : job.getSalaryMin());

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

            return vo;
        }).collect(java.util.stream.Collectors.toList()));

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "订单详情", notes = "获取订单详细信息")
    @GetMapping("/order/detail/{id}")
    public BaseResponse<OrderDetailVO> getOrderDetail(@PathVariable Long id) {
        OdOrders order = odOrdersService.getById(id);
        if (order == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        OrderDetailVO detailVO = new OrderDetailVO();
        detailVO.setId(order.getId());
        detailVO.setJobId(order.getJobId());
        detailVO.setOrderStatus(order.getOrderStatus());
        detailVO.setOrderStatusDesc(getOrderStatusDesc(order.getOrderStatus()));
        detailVO.setTradeMode(order.getTradeMode());
        detailVO.setTradeModeDesc(getTradeModeDesc(order.getTradeMode()));
        detailVO.setCreatedAt(order.getCreatedAt());
        detailVO.setStartTime(order.getStartTime());
        detailVO.setEndTime(order.getEndTime());
        detailVO.setCompletedAt(order.getCompletedAt());
        detailVO.setPenaltyAmount(order.getPenaltyAmount());
        detailVO.setApplicationId(order.getApplicationId());
        detailVO.setUserId(order.getUserId());
        detailVO.setMerchantId(order.getMerchantId());

        JbJobs job = jbJobsService.getById(order.getJobId());
        if (job != null) {
            detailVO.setJobTitle(job.getTitle());
            detailVO.setSalaryMin(job.getSalaryMin());
            detailVO.setSalaryMax(job.getSalaryMax());

            if (job.getCoverImages() != null && !job.getCoverImages().isEmpty()) {
                try {
                    List<String> images = objectMapper.readValue(job.getCoverImages(), new TypeReference<List<String>>() {});
                    detailVO.setJobCovers(images);
                } catch (Exception e) {
                    detailVO.setJobCovers(Arrays.asList(job.getCoverImages()));
                }
            }
        }

        UrMerchantProfiles merchant = urMerchantProfilesService.getById(order.getMerchantId());
        if (merchant != null) {
            OrderDetailVO.MerchantInfo merchantInfo = new OrderDetailVO.MerchantInfo();
            merchantInfo.setId(merchant.getUserId());
            merchantInfo.setMerchantName(merchant.getCompanyName());
            merchantInfo.setBusinessLicense(merchant.getLicenseUrl());
            merchantInfo.setContactPhone(merchant.getContactPhone());
            merchantInfo.setAddress(merchant.getCompanyAddress());
            detailVO.setMerchantInfo(merchantInfo);
        }

        return ResultUtils.success(detailVO);
    }
}
