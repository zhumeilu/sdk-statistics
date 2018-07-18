package com.yishen.sdk.manager.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yishen.sdk.manager.IPermissionManager;
import com.yishen.sdk.mapper.PermissionMapper;
import com.yishen.sdk.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhumeilu on 2018/5/8.
 */
@Service
public class PermissionManagerImpl extends ServiceImpl<PermissionMapper,Permission> implements IPermissionManager {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public List<Permission> selectByRoleIds(List<Long> ids) {
        return permissionMapper.selectByRoleIds(ids);
    }
}
