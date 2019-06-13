package com.cas.demo.controller;

import com.cas.demo.conf.CASConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
public class CASController {

    @Autowired
    private CASConfig casConfig;

    @RequestMapping("index")
    public String index(ModelMap map) {
        map.addAttribute("name", "clien A");
        return "index";
    }

    @RequestMapping("hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        //使用cas退出成功后,跳转到http://www.client1.com:9001/logout/success
        return "redirect:"+casConfig.getClientLogoutUrl();

    }
    @RequestMapping("logout/success")
    public String logoutsuccess(HttpSession session) {
        return "logoutsuccess";
    }
}
