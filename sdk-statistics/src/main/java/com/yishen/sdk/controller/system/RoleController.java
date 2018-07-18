package com.yishen.sdk.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yishen.sdk.bean.AjaxResult;
import com.yishen.sdk.manager.IPermissionManager;
import com.yishen.sdk.manager.IRoleManager;
import com.yishen.sdk.manager.IRolePermissionManager;
import com.yishen.sdk.model.Permission;
import com.yishen.sdk.model.Role;
import com.yishen.sdk.model.RolePermission;
import com.yishen.sdk.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhumeilu on 2018/5/7.
 */
@Controller
public class RoleController {

    @Autowired
    private IRoleManager roleManager;

    @Autowired
    private IPermissionManager permissionManager;
    @Autowired
    private IRolePermissionManager rolePermissionManager;

    @RequestMapping("/role")
    public String role(){
        return "system/role";
    }

    @RequestMapping("/role/list")
    @ResponseBody
    public Object list(){
        Page<Role> rolePage = roleManager.selectPage(new Page<Role>(1, 10));
        List<Role> records = rolePage.getRecords();
        int total = rolePage.getTotal();
        AjaxResult pageResult = new AjaxResult();
        pageResult.setData(records);
        pageResult.setCode(0);
        pageResult.setCount(total);
        return pageResult;
    }

    @RequestMapping(value = "/role/add",method = RequestMethod.GET)
    public String add(Long id, ModelMap modelMap){
        if(id!=null){
            Role role = roleManager.selectById(id);
            modelMap.put("role",role);
        }
        return "system/roleAdd";
    }

    @RequestMapping(value = "/role/add",method = RequestMethod.POST)
    @ResponseBody
    public Object add(Role role){
        try{
            if(role.getId()!=null)
                roleManager.updateById(role);
            else{
                role.setCreateDate(new Date());
                roleManager.insert(role);
            }
            return new AjaxResult(true,"操作成功");
        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");
    }

    @RequestMapping(value = "/role/batchDelete",method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(@RequestParam(value = "ids[]")long[] ids){
        try{
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < ids.length; i++) {
                list.add(ids[i]);
            }
            if(!list.isEmpty()){

                roleManager.deleteBatchIds(list);
                return new AjaxResult(true,"操作成功");
            }
        }catch (Exception e){

        }
        return new AjaxResult(false,"操作失败");
    }
    @RequestMapping(value = "/role/delete",method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(Long id){
        try{
            roleManager.deleteById(id);
            return new AjaxResult(true,"操作成功");
        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");
    }

    @RequestMapping(value = "/role/rolePermission",method = RequestMethod.GET)
    public String rolePermission(Long id, ModelMap modelMap){
        if(id!=null){
            Role role = roleManager.selectById(id);
            modelMap.put("role",role);
        }
        List<Permission> permissions = permissionManager.selectList(new EntityWrapper<>());
        modelMap.put("permissionList",permissions);
        List<RolePermission> rolePermissionList = rolePermissionManager.selectList(new EntityWrapper<RolePermission>().eq("roleId", id));
        List<Long> permisisonIds = new ArrayList<>();
        rolePermissionList.forEach(rolePermission -> {
            permisisonIds.add(rolePermission.getPermissionId());
        });
        modelMap.put("permisisonIds",permisisonIds);
        return "system/rolePermission";
    }

    @RequestMapping(value = "/role/rolePermission",method = RequestMethod.POST)
    @ResponseBody
    public Object rolePermission(@RequestParam(value = "permissionids[]",required = false)Long[] ids,Long id){

        try{
            rolePermissionManager.delete(new EntityWrapper<RolePermission>().eq("roleId", id));
            List<RolePermission> rolePermissionList = new ArrayList<>();
            if(ids!=null){
                for (int i = 0; i < ids.length; i++) {
                    RolePermission permissionRole = new RolePermission();
                    permissionRole.setRoleId(id);
                    permissionRole.setPermissionId(ids[i]);
                    rolePermissionList.add(permissionRole);
                }
                rolePermissionManager.insertBatch(rolePermissionList);
            }
            return new AjaxResult(true,"操作成功");

        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");

    }
}
