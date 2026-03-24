package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.MsNotifications;
import com.springbootinit.service.MsNotificationsService;
import com.springbootinit.mapper.MsNotificationsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【ms_notifications(系统通知表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class MsNotificationsServiceImpl extends ServiceImpl<MsNotificationsMapper, MsNotifications>
    implements MsNotificationsService{

}




