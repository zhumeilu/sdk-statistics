package com.yishen.sdk.manager.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yishen.sdk.manager.IUserRoleManager;
import com.yishen.sdk.mapper.UserRoleMapper;
import com.yishen.sdk.model.UserRole;
import org.springframework.stereotype.Service;

/**
 * Created by zhumeilu on 2018/5/8.
 */
@Service
public class UserRoleManagerImpl extends ServiceImpl<UserRoleMapper,UserRole> implements IUserRoleManager {
}
