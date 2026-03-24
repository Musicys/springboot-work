package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.OdOrders;
import com.springbootinit.service.OdOrdersService;
import com.springbootinit.mapper.OdOrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【od_orders(订单主表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class OdOrdersServiceImpl extends ServiceImpl<OdOrdersMapper, OdOrders>
    implements OdOrdersService{

}




