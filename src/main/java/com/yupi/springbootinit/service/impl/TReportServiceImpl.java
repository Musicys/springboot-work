package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TReport;

import com.yupi.springbootinit.mapper.TReportMapper;
import com.yupi.springbootinit.service.TReportService;
import org.springframework.stereotype.Service;

/**
* @author 黎旺com.yupi.springbootinit.service
* @description 针对表【t_report(举报投诉表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TReportServiceImpl extends ServiceImpl<TReportMapper, TReport>
    implements TReportService {

}




