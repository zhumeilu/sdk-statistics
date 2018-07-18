package com.yishen.sdk.manager.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yishen.sdk.manager.ISystemLogManager;
import com.yishen.sdk.mapper.SystemLogMapper;
import com.yishen.sdk.model.SystemLog;
import org.springframework.stereotype.Service;

/**
 * Created by zhumeilu on 2018/5/8.
 */
@Service
public class SystemLogManagerImpl extends ServiceImpl<SystemLogMapper,SystemLog> implements ISystemLogManager {
}
