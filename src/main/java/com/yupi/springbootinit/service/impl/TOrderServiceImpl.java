package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TOrder;

import com.yupi.springbootinit.mapper.TOrderMapper;
import com.yupi.springbootinit.service.TOrderService;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_order(兼职订单表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder>
    implements TOrderService {

}




