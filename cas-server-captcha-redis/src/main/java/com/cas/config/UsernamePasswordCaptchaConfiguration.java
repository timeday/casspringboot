package com.cas.config;

import com.cas.authentication.UsernamePasswordCaptchaAuthenticationHandler;
import com.cas.service.UserService;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//增加了@Import({DataSourceAutoConfiguration.class}),开启Spring Boot数据源自动配置，这样才可以使用使用jdbcTemplate
@Import({DataSourceAutoConfiguration.class})
@Configuration("usernamePasswordCaptchaconFiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class UsernamePasswordCaptchaConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Autowired
    private UserService userService;


    @Bean
    public AuthenticationHandler rememberMeUsernamePasswordCaptchaAuthenticationHandler() {

        UsernamePasswordCaptchaAuthenticationHandler handler = new UsernamePasswordCaptchaAuthenticationHandler(
                UsernamePasswordCaptchaAuthenticationHandler.class.getSimpleName(),
                servicesManager,
                new DefaultPrincipalFactory(),
                9);
        handler.setUserService(userService);
        return handler;

    }

    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(rememberMeUsernamePasswordCaptchaAuthenticationHandler());
    }
}
