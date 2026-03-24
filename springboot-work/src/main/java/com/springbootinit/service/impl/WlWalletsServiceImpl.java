package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.WlWallets;
import com.springbootinit.service.WlWalletsService;
import com.springbootinit.mapper.WlWalletsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【wl_wallets(钱包账户表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class WlWalletsServiceImpl extends ServiceImpl<WlWalletsMapper, WlWallets>
    implements WlWalletsService{

}




