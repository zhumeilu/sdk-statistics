package com.yishen.sdk.manager;

import com.baomidou.mybatisplus.service.IService;
import com.yishen.sdk.model.Permission;

import java.util.List;

/**
 * Created by zhumeilu on 2018/5/8.
 */
public interface IPermissionManager extends IService<Permission>{
    List<Permission> selectByRoleIds(List<Long> ids);
}
