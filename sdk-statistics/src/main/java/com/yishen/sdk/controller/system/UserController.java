package com.yishen.sdk.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yishen.sdk.bean.AjaxResult;
import com.yishen.sdk.manager.IRoleManager;
import com.yishen.sdk.manager.IUserManager;
import com.yishen.sdk.manager.IUserRoleManager;
import com.yishen.sdk.model.Role;
import com.yishen.sdk.model.User;
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
import java.util.UUID;

/**
 * Created by zhumeilu on 2018/5/7.
 */
@Controller
public class UserController {

    @Autowired
    private IUserManager userManager;
    @Autowired
    private IUserRoleManager userRoleManager;
    @Autowired
    private IRoleManager roleManager;
    @RequestMapping("/user")
    public String user(){
        return "system/user";
    }

    @RequestMapping("/user/list")
    @ResponseBody
    public Object list(){
        Page<User> userPage = userManager.selectPage(new Page<User>(1, 10));
        List<User> records = userPage.getRecords();
        int total = userPage.getTotal();
        AjaxResult pageResult = new AjaxResult();
        pageResult.setData(records);
        pageResult.setCode(0);
        pageResult.setCount(total);
        return pageResult;
    }

    @RequestMapping(value = "/user/add",method = RequestMethod.GET)
    public String add(Long id, ModelMap modelMap){
        if(id!=null){
            User user = userManager.selectById(id);
            modelMap.put("user",user);
        }
        System.out.println("user/add/get");
        return "system/userAdd";
    }

    @RequestMapping(value = "/user/add",method = RequestMethod.POST)
    @ResponseBody
    public Object add(User user){
        try{
            System.out.println("user/add/post");
            System.out.println(user);
            if(user.getId()!=null)
                userManager.updateById(user);
            else{
                user.setUserId(UUID.randomUUID().toString().replace("-",""));
                user.setCreateDate(new Date());
                userManager.insert(user);
            }
            return new AjaxResult(true,"操作成功");
        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");
    }

    @RequestMapping(value = "/user/batchDelete",method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(@RequestParam(value = "ids[]")long[] ids){
        try{
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < ids.length; i++) {
                list.add(ids[i]);
            }
            if(!list.isEmpty()){

                userManager.deleteBatchIds(list);
                return new AjaxResult(true,"操作成功");
            }
        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");
    }
    @RequestMapping(value = "/user/delete",method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(Long id){
        try{
            userManager.deleteById(id);
            return new AjaxResult(true,"操作成功");
        }catch (Exception e){

        }
        return new AjaxResult(false,"操作失败");
    }

    @RequestMapping(value = "/user/userRole",method = RequestMethod.GET)
    public String userRole(Long id, ModelMap modelMap){
        User user = userManager.selectById(id);
        modelMap.put("user",user);
        List<Role> roleList = roleManager.selectList(new EntityWrapper<>());
        modelMap.put("roleList",roleList);
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("userId",id);
        List<UserRole> userRoleList = userRoleManager.selectList(entityWrapper);
        List<Long> userRoleIds = new ArrayList<>();
        userRoleList.forEach(userRole->{
            userRoleIds.add(userRole.getRoleId());
        });
        modelMap.put("userRoleList",userRoleList);
        modelMap.put("userRoleIds",userRoleIds);
        return "system/userRole";
    }
    @RequestMapping(value = "/user/userRole",method = RequestMethod.POST)
    @ResponseBody
    public Object userRole(@RequestParam(value = "roleids",required = false)Long[] ids,Long id){

        try{
            boolean userId = userRoleManager.delete(new EntityWrapper<UserRole>().eq("userId", id));
            List<UserRole> userRoles = new ArrayList<>();
            if(ids!=null){
                for (int i = 0; i < ids.length; i++) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(id);
                    userRole.setRoleId(ids[i]);
                    userRoles.add(userRole);
                }
                userRoleManager.insertBatch(userRoles);
            }
            return new AjaxResult(true,"操作成功");

        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");

    }
}
