package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TStudentProfile;
import com.yupi.springbootinit.service.TStudentProfileService;
import com.yupi.springbootinit.mapper.TStudentProfileMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_student_profile(学生实名信息表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TStudentProfileServiceImpl extends ServiceImpl<TStudentProfileMapper, TStudentProfile>
    implements TStudentProfileService{

}




