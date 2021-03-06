package com.cas.demo.conf;


import com.cas.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * shiro集成cas之后，进行的配置
 */
public class MyShiroCasRealm extends CasRealm {
    @Autowired
    private UserService userInfoService;
    private static final Logger logger = LoggerFactory.getLogger(MyShiroCasRealm.class);

    /**
     * 用于授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进行授权 -------->");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String name = (String) super.getAvailablePrincipal(principalCollection);
       //TODO 角色信息等
        return info;
    }

    /**
     * 用于认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        调用父类的认证，父类认证已经完成了
        AuthenticationInfo authenticationInfo = super.doGetAuthenticationInfo(authenticationToken);
        String name = (String) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        SecurityUtils.getSubject().getSession().setAttribute("no", name);
       // 由于执行之前被CAS拦截过，所以一定是认证过，此处使用一个固定值作为任意帐号密码使用于Shiro认证
        return new SimpleAuthenticationInfo(name, "DEFULT_PASSWORD", this.getName());
    }
}
