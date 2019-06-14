package com.cas.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @Project casspringboot
 * @Package com.cas.demo.controller
 * @ClassName UserController
 * @Descripition TODO
 * @Author able
 * @Date 2019/6/14 13:28
 * @Version 1.0
 **/
import javax.servlet.http.HttpSession;

/**
 * @author: wangsaichao
 * @date: 2018/8/1
 * @description: 用户相关操作controller
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("insert")
    public String insert(String username){

        userService.insert(username);
        return "result";
    }

    /**
     * 跳转到默认页面
     * @param session
     * @return
     */
    @RequestMapping("/logout1")
    public String loginOut(HttpSession session){
        //注意：每个退出方法内都有一个session.invalidate();在点击退出的时候,
        // 销毁当前服务的session,如果没有配置这一行代码,你会发现,点击退出之后,还需要刷新一下连接才能重新跳转回登录页。
        session.invalidate();
        //这个是直接退出，走的是默认退出方式
        return "redirect:http://www.server.com:8443/cas/logout";
    }

    /**
     * 跳转到指定页面
     * @param session
     * @return
     */
    @RequestMapping("/logout2")
    public String loginOut2(HttpSession session){
        session.invalidate();
        //退出登录后，跳转到退成成功的页面，不走默认页面
        return "redirect:http://www.server.com:8443/cas/logout?service=http://www,client1.com:9003/cas/index";
    }

}
