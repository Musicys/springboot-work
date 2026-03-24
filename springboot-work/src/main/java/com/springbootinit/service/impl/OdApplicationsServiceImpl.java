package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.OdApplications;
import com.springbootinit.service.OdApplicationsService;
import com.springbootinit.mapper.OdApplicationsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【od_applications(投递记录表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class OdApplicationsServiceImpl extends ServiceImpl<OdApplicationsMapper, OdApplications>
    implements OdApplicationsService{

}




