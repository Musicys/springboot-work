package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.MsMessages;
import com.springbootinit.service.MsMessagesService;
import com.springbootinit.mapper.MsMessagesMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【ms_messages(消息内容表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class MsMessagesServiceImpl extends ServiceImpl<MsMessagesMapper, MsMessages>
    implements MsMessagesService{

}




