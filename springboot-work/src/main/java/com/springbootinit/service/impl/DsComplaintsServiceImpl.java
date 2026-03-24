package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.DsComplaints;
import com.springbootinit.service.DsComplaintsService;
import com.springbootinit.mapper.DsComplaintsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【ds_complaints(投诉主表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class DsComplaintsServiceImpl extends ServiceImpl<DsComplaintsMapper, DsComplaints>
    implements DsComplaintsService{

}




