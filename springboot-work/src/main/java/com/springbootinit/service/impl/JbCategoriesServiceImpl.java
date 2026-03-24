package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.JbCategories;
import com.springbootinit.service.JbCategoriesService;
import com.springbootinit.mapper.JbCategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【jb_categories(分类表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class JbCategoriesServiceImpl extends ServiceImpl<JbCategoriesMapper, JbCategories>
    implements JbCategoriesService{

}




