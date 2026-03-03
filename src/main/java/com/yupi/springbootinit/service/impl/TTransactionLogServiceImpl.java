package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.domain.TTransactionLog;
import com.yupi.springbootinit.service.TTransactionLogService;
import com.yupi.springbootinit.mapper.TTransactionLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【t_transaction_log(虚拟交易流水表)】的数据库操作Service实现
* @createDate 2026-03-03 09:11:36
*/
@Service
public class TTransactionLogServiceImpl extends ServiceImpl<TTransactionLogMapper, TTransactionLog>
    implements TTransactionLogService{

}




