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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家端兼职接口
 */
@Api(tags = "商家端兼职接口")
@RestController
@RequestMapping("/pro/job/job")
public class ProJobLstController {

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

    @ApiOperation(value = "获取兼职岗位列表", notes = "获取当前商家的兼职岗位列表，支持分页和关键词搜索")
    @PostMapping("/list")
    public BaseResponse<Page<JbJobs>> getJobList(
            @RequestBody JobQueryDTO queryDTO,
            HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        Page<JbJobs> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        QueryWrapper<JbJobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchant_id", userId);

        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            queryWrapper.like("title", queryDTO.getKeyword());
        }

        queryWrapper.orderByDesc("created_at");
        Page<JbJobs> resultPage = jbJobsService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "获取待审核的兼职岗位列表", notes = "获取当前商家待审核的兼职岗位列表")
    @PostMapping("/list/pending")
    public BaseResponse<Page<JbJobs>> getPendingJobList(
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

        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            queryWrapper.like("title", queryDTO.getKeyword());
        }

        queryWrapper.orderByDesc("created_at");
        Page<JbJobs> resultPage = jbJobsService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "获取已发布的兼职岗位列表", notes = "获取当前商家已发布的兼职岗位列表")
    @PostMapping("/list/published")
    public BaseResponse<Page<JbJobs>> getPublishedJobList(
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

        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            queryWrapper.like("title", queryDTO.getKeyword());
        }

        queryWrapper.orderByDesc("created_at");
        Page<JbJobs> resultPage = jbJobsService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }

    @ApiOperation(value = "获取兼职岗位详情", notes = "获取兼职岗位详细信息")
    @GetMapping("/get/{id}")
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
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权查看该岗位");
        }

        JobDetailVO vo = new JobDetailVO();
        BeanUtils.copyProperties(job, vo);

        return ResultUtils.success(vo);
    }

    @ApiOperation(value = "发布新兼职岗位", notes = "商家发布新的兼职岗位")
    @PostMapping("/add")
    public BaseResponse<Long> addJob(@RequestBody ProJobPublishRequest publishRequest, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        JbJobs job = new JbJobs();
        job.setMerchantId(userId);
        job.setTitle(publishRequest.getTitle());
        job.setDescription(publishRequest.getDescription());
        job.setSalaryMin(publishRequest.getSalaryMin());
        job.setSalaryMax(publishRequest.getSalaryMax());
        job.setCategoryId(Math.toIntExact(publishRequest.getCategoryId()));
        job.setRegionCode(publishRequest.getRegionCode());
        job.setRegionName(publishRequest.getRegionName());
        job.setDepositAmount(publishRequest.getDepositAmount());
        job.setTradeMode(publishRequest.getTradeMode());
        job.setSettlementCycle(Integer.valueOf(publishRequest.getSettlementCycle()));
        job.setJobType(publishRequest.getJobType());
        job.setWorkTimeType(publishRequest.getWorkTimeType());
        job.setWorkTimeDesc(publishRequest.getWorkTimeDesc());
        job.setBriefIntro(publishRequest.getBriefIntro());
        job.setJobDetail(publishRequest.getDescription());
          job.setCoverImages(publishRequest.getCoverImages());
        job.setJobStatus(0);
        job.setStatus(1);
        job.setPublishTime(new Date());

        boolean saved = jbJobsService.save(job);
        if (!saved) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "发布失败");
        }

        return ResultUtils.success(job.getId());
    }

    @ApiOperation(value = "更新兼职岗位", notes = "商家更新兼职岗位信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateJob(@RequestBody ProJobPublishRequest publishRequest, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        if (publishRequest.getId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "岗位ID不能为空");
        }

        JbJobs existJob = jbJobsService.getById(publishRequest.getId());
        if (existJob == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "岗位不存在");
        }

        if (!existJob.getMerchantId().equals(userId)) {
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权修改该岗位");
        }

        if (StringUtils.isNotBlank(publishRequest.getTitle())) {
            existJob.setTitle(publishRequest.getTitle());
        }
        if (StringUtils.isNotBlank(publishRequest.getDescription())) {
            existJob.setDescription(publishRequest.getDescription());
        }
        if (publishRequest.getSalaryMin() != null) {
            existJob.setSalaryMin(publishRequest.getSalaryMin());
        }
        if (publishRequest.getSalaryMax() != null) {
            existJob.setSalaryMax(publishRequest.getSalaryMax());
        }
        if (publishRequest.getCategoryId() != null) {
            existJob.setCategoryId(Math.toIntExact(publishRequest.getCategoryId()));
        }
        if (StringUtils.isNotBlank(publishRequest.getRegionCode())) {
            existJob.setRegionCode(publishRequest.getRegionCode());
        }
        if (StringUtils.isNotBlank(publishRequest.getRegionName())) {
            existJob.setRegionName(publishRequest.getRegionName());
        }
        if (publishRequest.getDepositAmount() != null) {
            existJob.setDepositAmount(publishRequest.getDepositAmount());
        }
        if (publishRequest.getTradeMode() != null) {
            existJob.setTradeMode(publishRequest.getTradeMode());
        }
        if (publishRequest.getSettlementCycle() != null) {
            existJob.setSettlementCycle(Integer.valueOf(publishRequest.getSettlementCycle()));
        }
        if (publishRequest.getJobType() != null) {
            existJob.setJobType(publishRequest.getJobType());
        }
        if (publishRequest.getWorkTimeType() != null) {
            existJob.setWorkTimeType(publishRequest.getWorkTimeType());
        }
        if (StringUtils.isNotBlank(publishRequest.getWorkTimeDesc())) {
            existJob.setWorkTimeDesc(publishRequest.getWorkTimeDesc());
        }
        if (StringUtils.isNotBlank(publishRequest.getBriefIntro())) {
            existJob.setBriefIntro(publishRequest.getBriefIntro());
        }

        if (publishRequest.getRecruitNum() != null) {
            existJob.setRecruitNum(publishRequest.getRecruitNum());
        }

        boolean updated = jbJobsService.updateById(existJob);
        return ResultUtils.success(updated);
    }

    @ApiOperation(value = "删除兼职岗位", notes = "商家删除兼职岗位")
    @PostMapping("/delete/{id}")
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
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权删除该岗位");
        }

        boolean removed = jbJobsService.removeById(id);
        return ResultUtils.success(removed);
    }

    @ApiOperation(value = "下架兼职岗位", notes = "商家下架兼职岗位")
    @PostMapping("/close/{id}")
    public BaseResponse<Boolean> closeJob(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        JbJobs job = jbJobsService.getById(id);
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "岗位不存在");
        }

        if (!job.getMerchantId().equals(userId)) {
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权操作该岗位");
        }

        job.setStatus(4);
        boolean updated = jbJobsService.updateById(job);
        return ResultUtils.success(updated);
    }

    @ApiOperation(value = "上架兼职岗位", notes = "商家上架兼职岗位")
    @PostMapping("/open/{id}")
    public BaseResponse<Boolean> openJob(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromCookie(request);
        if (userId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        JbJobs job = jbJobsService.getById(id);
        if (job == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "岗位不存在");
        }

        if (!job.getMerchantId().equals(userId)) {
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权操作该岗位");
        }

        if (job.getJobStatus() != 1) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "审核未通过，无法上架");
        }

        job.setStatus(1);
        boolean updated = jbJobsService.updateById(job);
        return ResultUtils.success(updated);
    }
}
