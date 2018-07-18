package com.yishen.sdk.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
@Data
@TableName("role_permission")
@Alias("RolePermission")
public class RolePermission implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated
     */
    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    /**
     * 角色编号
     *
     * @mbg.generated
     */
    private Long roleId;

    /**
     * 权限编号
     *
     * @mbg.generated
     */
    private Long permissionId;

    private static final long serialVersionUID = 1L;


}