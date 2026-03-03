package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TWallet;
import com.yupi.springbootinit.service.TWalletService;
import com.yupi.springbootinit.mapper.TWalletMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_wallet(用户虚拟钱包表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TWalletServiceImpl extends ServiceImpl<TWalletMapper, TWallet>
    implements TWalletService{

}




