package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TUserRole;
import com.yupi.springbootinit.service.TUserRoleService;
import com.yupi.springbootinit.mapper.TUserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TUserRoleServiceImpl extends ServiceImpl<TUserRoleMapper, TUserRole>
    implements TUserRoleService{

}




