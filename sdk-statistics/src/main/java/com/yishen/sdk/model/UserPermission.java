package com.yishen.sdk.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
@TableName("user_permission")
@Alias("UserPermission")
@Data
public class UserPermission implements Serializable {
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
    private Integer userId;

    /**
     * 权限编号
     *
     * @mbg.generated
     */
    private Integer permissionId;

    /**
     * 权限类型(-1:减权限,1:增权限)
     *
     * @mbg.generated
     */
    private Byte type;

    private static final long serialVersionUID = 1L;


}