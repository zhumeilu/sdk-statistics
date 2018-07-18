package com.yishen.sdk.manager.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yishen.sdk.manager.IRolePermissionManager;
import com.yishen.sdk.mapper.RolePermissionMapper;
import com.yishen.sdk.model.RolePermission;
import org.springframework.stereotype.Service;

/**
 * Created by zhumeilu on 2018/5/8.
 */
@Service
public class RolePermissionManagerImpl extends ServiceImpl<RolePermissionMapper,RolePermission> implements IRolePermissionManager {
}
