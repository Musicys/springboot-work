package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TSysDict;
import com.yupi.springbootinit.service.TSysDictService;
import com.yupi.springbootinit.mapper.TSysDictMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_sys_dict(系统字典表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TSysDictServiceImpl extends ServiceImpl<TSysDictMapper, TSysDict>
    implements TSysDictService{

}




