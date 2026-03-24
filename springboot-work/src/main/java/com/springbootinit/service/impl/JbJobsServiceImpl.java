package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.JbJobs;
import com.springbootinit.service.JbJobsService;
import com.springbootinit.mapper.JbJobsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【jb_jobs(兼职岗位表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class JbJobsServiceImpl extends ServiceImpl<JbJobsMapper, JbJobs>
    implements JbJobsService{

}




