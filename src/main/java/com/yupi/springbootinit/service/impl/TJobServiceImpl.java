package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TJob;

import com.yupi.springbootinit.mapper.TJobMapper;
import com.yupi.springbootinit.service.TJobService;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_job(兼职任务表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TJobServiceImpl extends ServiceImpl<TJobMapper, TJob>
    implements TJobService {

}




