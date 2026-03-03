package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TUser;
import com.yupi.springbootinit.service.TUserService;
import com.yupi.springbootinit.mapper.TUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_user(用户基础表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
    implements TUserService{

}




