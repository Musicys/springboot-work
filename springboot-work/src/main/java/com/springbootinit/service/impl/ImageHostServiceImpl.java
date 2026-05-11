package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.ImageHost;
import com.springbootinit.service.ImageHostService;
import com.springbootinit.mapper.ImageHostMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【image_host(图床图片信息表)】的数据库操作Service实现
* @createDate 2026-05-08 19:19:18
*/
@Service
public class ImageHostServiceImpl extends ServiceImpl<ImageHostMapper, ImageHost>
    implements ImageHostService{

}




