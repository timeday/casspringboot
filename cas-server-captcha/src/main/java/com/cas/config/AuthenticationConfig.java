package com.cas.config;

import com.cas.authentication.MyUserNamePasswordAuthenticationHandler;
import com.cas.service.UserService;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/*
 * @Project casspringboot
 * @Package com.cas.authentication
 * @ClassName AuthenticationConfig
 * @Descripition 注册自定义验证器
 * @Author able
 * @Date 2019/6/13 17:17
 * @Version 1.0
 */
//增加了@Import({DataSourceAutoConfiguration.class}),开启Spring Boot数据源自动配置，这样才可以使用使用jdbcTemplate
@Import({DataSourceAutoConfiguration.class})
@Configuration("myAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class AuthenticationConfig implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Autowired
    @Qualifier("jdbcPrincipalFactory")
    public PrincipalFactory jdbcPrincipalFactory;

    @Autowired
    private UserService userService;
     /*
     * 注册验证器
     *
     * @return
     */

    @Bean
    public AuthenticationHandler customAuthenticationHandler() {
        //优先验证
        MyUserNamePasswordAuthenticationHandler handler = new MyUserNamePasswordAuthenticationHandler(MyUserNamePasswordAuthenticationHandler.class.getSimpleName(), servicesManager, new DefaultPrincipalFactory(), 1);
        //此处需要设置 如果不设置则不生效
        handler.setUserService(userService);

        return handler;

    }

    //注册自定义认证器
    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(customAuthenticationHandler());
    }
}
