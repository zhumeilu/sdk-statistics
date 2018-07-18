package com.yishen.sdk.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yishen.sdk.bean.AjaxResult;
import com.yishen.sdk.manager.IPermissionManager;
import com.yishen.sdk.model.Permission;
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
public class PermissionController {

    @Autowired
    private IPermissionManager permissionManager;

    @RequestMapping("/permission")
    public String permission(){
        return "system/permission";
    }

    @RequestMapping("/permission/list")
    @ResponseBody
    public Object list(){
        Page<Permission> permissionPage = permissionManager.selectPage(new Page<Permission>(1, 10));
        List<Permission> records = permissionPage.getRecords();
        int total = permissionPage.getTotal();
        AjaxResult pageResult = new AjaxResult();
        pageResult.setData(records);
        pageResult.setCode(0);
        pageResult.setCount(total);
        return pageResult;
    }

    @RequestMapping(value = "/permission/add",method = RequestMethod.GET)
    public String add(Long id, ModelMap modelMap){
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("type","1");
        List list = permissionManager.selectList(entityWrapper);
        modelMap.put("pList",list);
        if(id!=null){
            Permission permission = permissionManager.selectById(id);
            modelMap.put("permission",permission);
        }
        return "system/permissionAdd";
    }

    @RequestMapping(value = "/permission/add",method = RequestMethod.POST)
    @ResponseBody
    public Object add(Permission permission){
        try{
            System.out.println("permission/add/post");
            System.out.println(permission);
            if(permission.getId()!=null)
                permissionManager.updateById(permission);
            else{
                permission.setCreateDate(new Date());
                permissionManager.insert(permission);
            }
            return new AjaxResult(true,"操作成功");
        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");
    }

    @RequestMapping(value = "/permission/batchDelete",method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(@RequestParam(value = "ids[]")long[] ids){
        try{
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < ids.length; i++) {
                list.add(ids[i]);
            }
            if(!list.isEmpty()){

                permissionManager.deleteBatchIds(list);
                return new AjaxResult(true,"操作成功");
            }
        }catch (Exception e){

            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");
    }
    @RequestMapping(value = "/permission/delete",method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(Long id){
        try{
            permissionManager.deleteById(id);
            return new AjaxResult(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new AjaxResult(false,"操作失败");
    }

}
