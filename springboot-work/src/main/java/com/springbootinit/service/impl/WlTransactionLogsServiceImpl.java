package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.model.domain.WlTransactionLogs;
import com.springbootinit.service.WlTransactionLogsService;
import com.springbootinit.mapper.WlTransactionLogsMapper;
import org.springframework.stereotype.Service;

/**
* @author 黎旺
* @description 针对表【wl_transaction_logs(交易流水表)】的数据库操作Service实现
* @createDate 2026-03-20 09:41:36
*/
@Service
public class WlTransactionLogsServiceImpl extends ServiceImpl<WlTransactionLogsMapper, WlTransactionLogs>
    implements WlTransactionLogsService{

}




