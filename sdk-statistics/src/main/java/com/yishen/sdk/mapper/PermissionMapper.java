package com.yishen.sdk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yishen.sdk.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: zhumeilu
 * Date: 2017/6/30
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> selectByRoleIds(@Param("ids") List<Long> ids);

}
