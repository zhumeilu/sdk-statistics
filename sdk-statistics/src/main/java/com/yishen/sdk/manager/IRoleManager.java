package com.yishen.sdk.manager;

import com.baomidou.mybatisplus.service.IService;
import com.yishen.sdk.model.Role;
import com.yishen.sdk.model.User;

import java.util.List;

/**
 * Created by zhumeilu on 2018/5/8.
 */
public interface IRoleManager extends IService<Role>{

    List<Role> selectRoleByUserId(Long idz);
}
