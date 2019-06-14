package com.cas.authentication;

import com.cas.config.RememberMeUsernamePasswordCaptchaCredential;
import com.cas.exception.AccountCodeException;
import com.cas.exception.AccountRoleException;
import com.cas.exception.CaptchaException;
import com.cas.passwordEncode.MyEncoder;
import com.cas.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Map;


public class UsernamePasswordCaptchaAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {


    private UserService userService;


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UsernamePasswordCaptchaAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        RememberMeUsernamePasswordCaptchaCredential myCredential = (RememberMeUsernamePasswordCaptchaCredential) credential;
        String requestCaptcha = myCredential.getCaptcha();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object attribute = attributes.getRequest().getSession().getAttribute("captcha");

        String realCaptcha = attribute == null ? null : attribute.toString();

        if(StringUtils.isBlank(requestCaptcha) || !requestCaptcha.toUpperCase().equals(realCaptcha)){
            throw new CaptchaException("验证码错误");
        }
        String username = myCredential.getUsername();
        MyEncoder myEncoder=new MyEncoder();
        String password = myCredential.getUsername();
        Map<String, Object> user = userService.findByUserName(username);

        //可以在这里直接对用户名校验,或者调用 CredentialsMatcher 校验
        if (user == null) {
            throw new AccountRoleException("用户不存在！");
        }
        CharSequence cs=user.get("password").toString();
        //这里将 密码对比 注销掉,否则 无法锁定  要将密码对比 交给 密码比较器 在这里可以添加自己的密码比较器等
        if (myEncoder.matches(cs,password)) {
            throw new AccountCodeException("用户名或密码错误！");
        }
        if ("1".equals(user.get("state"))) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }
        return createHandlerResult(credential, this.principalFactory.createPrincipal(username),new ArrayList<>(0));
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof RememberMeUsernamePasswordCaptchaCredential;
    }
}
