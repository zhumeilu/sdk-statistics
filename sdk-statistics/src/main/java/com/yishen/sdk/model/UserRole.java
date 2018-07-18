package com.yishen.sdk.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
@TableName("user_role")
@Alias("UserRole")
@Data
public class UserRole implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated
     */
    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     * 角色编号
     *
     * @mbg.generated
     */
    private Long roleId;

    private static final long serialVersionUID = 1L;


}