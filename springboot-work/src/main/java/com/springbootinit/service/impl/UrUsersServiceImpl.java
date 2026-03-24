package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.service.UrUsersService;
import com.springbootinit.mapper.UrUsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【ur_users(用户基础表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class UrUsersServiceImpl extends ServiceImpl<UrUsersMapper, UrUsers>
    implements UrUsersService{

}




