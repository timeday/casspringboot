package com.cas.demo.controller;

import com.cas.demo.conf.CASConfig;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
public class CASController {

    @Autowired
    private CASConfig casConfig;

    @RequestMapping("index")
    public String index(ModelMap map) {
        map.addAttribute("name", "clien C");
        return "index";
    }

    @RequestMapping("hello")
    public String hello(HttpServletRequest request) {
        //获取用户属性
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        for (String key : attributes.keySet()) {
            System.out.println(key + "/" + attributes.get(key));
        }
        return "hello";
    }

    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        //使用cas退出成功后,跳转到http://www.client1.com:9003/logout/success
        return "redirect:"+casConfig.getClientLogoutUrl();

    }
    @RequestMapping("logout/success")
    public String logoutsuccess(HttpSession session) {
        return "logoutsuccess";
    }
}
