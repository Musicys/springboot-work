package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.JbHomeConfigs;
import com.springbootinit.service.JbHomeConfigsService;
import com.springbootinit.mapper.JbHomeConfigsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【jb_home_configs(首页装修配置表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class JbHomeConfigsServiceImpl extends ServiceImpl<JbHomeConfigsMapper, JbHomeConfigs>
    implements JbHomeConfigsService{

}




