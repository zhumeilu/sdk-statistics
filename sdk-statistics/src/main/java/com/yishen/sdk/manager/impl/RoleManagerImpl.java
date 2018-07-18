package com.yishen.sdk.manager.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yishen.sdk.manager.IRoleManager;
import com.yishen.sdk.mapper.RoleMapper;
import com.yishen.sdk.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhumeilu on 2018/5/8.
 */
@Service
public class RoleManagerImpl extends ServiceImpl<RoleMapper,Role> implements IRoleManager {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<Role> selectRoleByUserId(Long idz) {

        return null;
    }
}
