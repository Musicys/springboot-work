package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.MsConversations;
import com.springbootinit.service.MsConversationsService;
import com.springbootinit.mapper.MsConversationsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【ms_conversations(会话表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class MsConversationsServiceImpl extends ServiceImpl<MsConversationsMapper, MsConversations>
    implements MsConversationsService{

}




