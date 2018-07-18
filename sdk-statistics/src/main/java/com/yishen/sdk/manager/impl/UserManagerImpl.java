package com.yishen.sdk.manager.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yishen.sdk.manager.IUserManager;
import com.yishen.sdk.mapper.UserMapper;
import com.yishen.sdk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhumeilu on 2018/5/8.
 */
@Service
public class UserManagerImpl extends ServiceImpl<UserMapper,User> implements IUserManager {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User selectUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userMapper.selectOne(user);
    }
}
