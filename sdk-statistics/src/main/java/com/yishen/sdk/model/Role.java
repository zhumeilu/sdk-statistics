package com.yishen.sdk.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("role")
@Alias("Role")
public class Role implements Serializable {
    @TableId(value="id",type= IdType.AUTO)
    private Long id;
    /**
     * 角色名称
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 角色标题
     *
     * @mbg.generated
     */
    private String title;

    /**
     * 角色描述
     *
     * @mbg.generated
     */
    private String description;

   private Date createDate;

    /**
     * 排序
     *
     * @mbg.generated
     */
    private Long orders;

    private static final long serialVersionUID = 1L;


}