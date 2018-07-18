package com.yishen.sdk.manager;

import com.baomidou.mybatisplus.service.IService;
import com.yishen.sdk.model.User;

/**
 * Created by zhumeilu on 2018/5/8.
 */
public interface IUserManager extends IService<User>{

    User selectUserByUsername(String username);
}
