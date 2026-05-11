package com.springbootinit.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.JbJobs;
import com.springbootinit.model.domain.UrMerchantProfiles;
import com.springbootinit.model.domain.WlWallets;
import com.springbootinit.model.dto.JobDetailDTO;
import com.springbootinit.model.dto.JobSearchWrapper;
import com.springbootinit.service.JbJobsService;
import com.springbootinit.service.UrMerchantProfilesService;
import com.springbootinit.service.WlWalletsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 管理员-兼职岗位管理接口
 */
@Api(tags = "管理员-兼职岗位管理")
@RestController
@RequestMapping("/admin/job")
public class AdminJobController {

    @Autowired
    private JbJobsService jbJobsService;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    @Autowired
    private WlWalletsService wlWalletsService;

    /**
     * 获取兼职岗位列表（分页）
     *
     * @param searchWrapper 搜索包装参数
     * @return 兼职岗位列表
     */
    @ApiOperation(value = "获取兼职岗位列表", notes = "分页获取兼职岗位列表，支持按多个字段搜索")
    @PostMapping("/list")
    public BaseResponse<Page<JbJobs>> getJobs(
            @ApiParam(value = "搜索包装参数") @RequestBody JobSearchWrapper searchWrapper) {
        Page<JbJobs> pageInfo = new Page<>(searchWrapper.getPage().getPageNum(), searchWrapper.getPage().getPageSize());
        QueryWrapper<JbJobs> queryWrapper = new QueryWrapper<>();
        
        // 搜索条件
        if (searchWrapper.getParams().getId() != null) {
            queryWrapper.eq("id", searchWrapper.getParams().getId());
        }
        if (searchWrapper.getParams().getTitle() != null && !searchWrapper.getParams().getTitle().isEmpty()) {
            queryWrapper.like("title", searchWrapper.getParams().getTitle());
        }
        if (searchWrapper.getParams().getMerchantId() != null) {
            queryWrapper.eq("merchant_id", searchWrapper.getParams().getMerchantId());
        }
        if (searchWrapper.getParams().getCategoryId() != null) {
            queryWrapper.eq("category_id", searchWrapper.getParams().getCategoryId());
        }
        if (searchWrapper.getParams().getRegionCode() != null && !searchWrapper.getParams().getRegionCode().isEmpty()) {
            queryWrapper.eq("region_code", searchWrapper.getParams().getRegionCode());
        }
        if (searchWrapper.getParams().getStatus() != null) {
            queryWrapper.eq("status", searchWrapper.getParams().getStatus());
        }
        if (searchWrapper.getParams().getTradeMode() != null) {
            queryWrapper.eq("trade_mode", searchWrapper.getParams().getTradeMode());
        }
        if (searchWrapper.getParams().getJobStatus() != null) {
            queryWrapper.eq("job_status", searchWrapper.getParams().getJobStatus());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc("created_at");
        
        Page<JbJobs> result = jbJobsService.page(pageInfo, queryWrapper);
        result.setSize(searchWrapper.getPage().getPageSize());
        result.setTotal(jbJobsService.count(queryWrapper));
        result.setCurrent(searchWrapper.getPage().getPageNum());
        return ResultUtils.success(result);
    }

    /**
     * 获取兼职岗位详情
     *
     * @param jobId 兼职岗位ID
     * @return 兼职岗位详情（包含商家档案和钱包信息）
     */
    @ApiOperation(value = "获取兼职岗位详情", notes = "根据ID获取兼职岗位详细信息（包含商家档案和钱包信息）")
    @PostMapping("/detail")
    public BaseResponse<JobDetailDTO> getJob(@ApiParam(value = "兼职岗位ID", required = true) @RequestParam Long jobId) {
        JbJobs job = jbJobsService.getById(jobId);
        if (job == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职岗位不存在");
        }
        JobDetailDTO dto = new JobDetailDTO();
        dto.setJob(job);
        
        if (job.getMerchantId() != null) {
            UrMerchantProfiles profile = urMerchantProfilesService.getById(job.getMerchantId());
            dto.setMerchantProfile(profile);
            
            WlWallets wallet = wlWalletsService.getById(job.getMerchantId());
            dto.setWallet(wallet);
        }
        
        return ResultUtils.success(dto);
    }

    /**
     * 创建兼职岗位
     *
     * @param job 兼职岗位信息
     * @return 创建结果
     */
    @ApiOperation(value = "创建兼职岗位", notes = "创建新的兼职岗位")
    @PostMapping("/create")
    public BaseResponse<Long> createJob(@ApiParam(value = "兼职岗位信息", required = true) @RequestBody JbJobs job) {
        if (job.getTitle() == null || job.getMerchantId() == null || job.getCategoryId() == null || job.getRegionCode() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        boolean save = jbJobsService.save(job);
        if (!save) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "创建失败");
        }
        return ResultUtils.success(job.getId());
    }

    /**
     * 更新兼职岗位
     *
     * @param job 兼职岗位信息
     * @return 更新结果
     */
    @ApiOperation(value = "更新兼职岗位", notes = "更新兼职岗位信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateJob(@ApiParam(value = "兼职岗位信息", required = true) @RequestBody JbJobs job) {
        if (job.getId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职岗位ID不能为空");
        }
        JbJobs existing = jbJobsService.getById(job.getId());
        if (existing == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职岗位不存在");
        }

        boolean update = jbJobsService.updateById(job);
        return ResultUtils.success(update);
    }

    /**
     * 删除兼职岗位
     *
     * @param jobId 兼职岗位ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除兼职岗位", notes = "删除兼职岗位")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteJob(@ApiParam(value = "兼职岗位ID", required = true) @RequestParam Long jobId) {
        JbJobs existing = jbJobsService.getById(jobId);
        if (existing == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职岗位不存在");
        }
        boolean delete = jbJobsService.removeById(jobId);
        return ResultUtils.success(delete);
    }

    /**
     * 导出兼职岗位列表到Excel
     */
    @ApiOperation(value = "导出兼职岗位列表", notes = "将兼职岗位列表导出为Excel文件")
    @PostMapping("/export")
    public void exportJobs(HttpServletResponse response) throws Exception {
        QueryWrapper<JbJobs> queryWrapper = new QueryWrapper<>();
        List<JbJobs> jobs = jbJobsService.list(queryWrapper);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("兼职岗位列表", "UTF-8") + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 使用EasyExcel导出
        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), JbJobs.class)
                .sheet("兼职岗位列表")
                .doWrite(jobs);
    }

    /**
     * 审核通过兼职岗位
     *
     * @param jobId 兼职岗位ID
     * @return 审核结果
     */
    @ApiOperation(value = "审核通过兼职岗位", notes = "将兼职岗位审核状态改为审核通过")
    @PostMapping("/approve")
    public BaseResponse<Boolean> approveJob(@ApiParam(value = "兼职岗位ID", required = true) @RequestParam Long jobId) {
        JbJobs existing = jbJobsService.getById(jobId);
        if (existing == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职岗位不存在");
        }
        if (existing.getJobStatus() != 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "只有待审核的岗位才能审核通过");
        }
        JbJobs job = new JbJobs();
        job.setId(jobId);
        job.setJobStatus(1);
        job.setStatus(2);
        boolean update = jbJobsService.updateById(job);
        return ResultUtils.success(update);
    }

    /**
     * 强制下架兼职岗位
     *
     * @param jobId 兼职岗位ID
     * @return 操作结果
     */
    @ApiOperation(value = "强制下架兼职岗位", notes = "将兼职岗位状态改为强制下架")
    @PostMapping("/forceClose")
    public BaseResponse<Boolean> forceCloseJob(@ApiParam(value = "兼职岗位ID", required = true) @RequestParam Long jobId) {
        JbJobs existing = jbJobsService.getById(jobId);
        if (existing == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职岗位不存在");
        }
        JbJobs job = new JbJobs();
        job.setId(jobId);
        job.setStatus(5);
        boolean update = jbJobsService.updateById(job);
        return ResultUtils.success(update);
    }

    /**
     * 更新兼职岗位状态
     *
     * @param request 更新状态请求
     * @return 操作结果
     */
    @ApiOperation(value = "更新兼职岗位状态", notes = "更新兼职岗位的状态")
    @PostMapping("/updateStatus")
    public BaseResponse<Boolean> updateJobStatus(@RequestBody UpdateStatusRequest request) {
        Long jobId = request.getJobId();
        Integer status = request.getStatus();
        
        JbJobs existing = jbJobsService.getById(jobId);
        if (existing == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "兼职岗位不存在");
        }
        JbJobs job = new JbJobs();
        job.setId(jobId);
        job.setStatus(status);
        boolean update = jbJobsService.updateById(job);
        return ResultUtils.success(update);
    }

    /**
     * 更新状态请求对象
     */
    public static class UpdateStatusRequest {
        private Long jobId;
        private Integer status;

        public Long getJobId() {
            return jobId;
        }

        public void setJobId(Long jobId) {
            this.jobId = jobId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

}