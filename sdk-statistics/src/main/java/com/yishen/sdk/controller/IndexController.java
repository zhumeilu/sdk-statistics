package com.yishen.sdk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhumeilu on 2018/5/7.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public Object index(){
        System.out.println("hello");
        return "index";
    }

}
