package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TStudio;
import com.yupi.springbootinit.service.TStudioService;
import com.yupi.springbootinit.mapper.TStudioMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_studio(工作室信息表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TStudioServiceImpl extends ServiceImpl<TStudioMapper, TStudio>
    implements TStudioService{

}




