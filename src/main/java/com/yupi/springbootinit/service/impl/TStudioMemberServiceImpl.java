package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TStudioMember;
import com.yupi.springbootinit.service.TStudioMemberService;
import com.yupi.springbootinit.mapper.TStudioMemberMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_studio_member(工作室成员表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TStudioMemberServiceImpl extends ServiceImpl<TStudioMemberMapper, TStudioMember>
    implements TStudioMemberService{

}




