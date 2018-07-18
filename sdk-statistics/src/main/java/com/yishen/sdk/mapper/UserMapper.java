package com.yishen.sdk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yishen.sdk.model.User;
import com.yishen.sdk.query.UserQueryObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: zhumeilu
 * Date: 2017/6/30
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User get(Long id);

    List<User> list(UserQueryObject qo);
}
