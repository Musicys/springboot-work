package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TRole;
import com.yupi.springbootinit.service.TRoleService;
import com.yupi.springbootinit.mapper.TRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_role(角色表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TRoleServiceImpl extends ServiceImpl<TRoleMapper, TRole>
    implements TRoleService{

}




