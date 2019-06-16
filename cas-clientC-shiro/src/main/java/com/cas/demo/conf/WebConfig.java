package com.cas.demo.conf;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CASConfig casConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    //跨域
    /*@Bean
    public FilterRegistrationBean crosFilterBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CrosFilter());
        filterRegistrationBean.setName("crosFilter");
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }*/

    /**
     * 登出过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterSingleRegistration() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casConfig.getServerUrlPrefix());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setName("singleSignOutFilter");
        filterRegistrationBean.setFilter(new DelegatingFilterProxy(singleSignOutFilter));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }

    /**
     * 授权过滤器，用于登录
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String>  initParameters = new HashMap<String, String>();
        initParameters.put("casServerLoginUrl", casConfig.getServerUrlPrefix());
        initParameters.put("serverName", casConfig.getServerName());
//        initParameters.put("ignorePattern", "/logout/success|/index");
        //表示过滤所有，SimpleUrlPatternMatcherStrategy过滤规则类
        initParameters.put("ignoreUrlPatternType", "SimpleUrlPatternMatcherStrategy");
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(3);
        return registration;
    }


    /**
     * ticke验证器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterValidationRegistration() {
        Cas20ProxyReceivingTicketValidationFilter validationFilter = new Cas20ProxyReceivingTicketValidationFilter();
        validationFilter.setServerName(casConfig.getServerName());
        validationFilter.setTicketValidator(ticketValidator());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setName("validationFilter");
        filterRegistrationBean.setFilter(new DelegatingFilterProxy(validationFilter));
        filterRegistrationBean.setOrder(4);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    @Bean
    public Cas20ProxyTicketValidator ticketValidator() {
        Cas20ProxyTicketValidator ticketValidator = new Cas20ProxyTicketValidator(casConfig.getServerUrlPrefix());
        ticketValidator.setEncoding("UTF-8");
        return ticketValidator;
    }

    /**
     * wraper过滤器
     * 该过滤器负责实现HttpServletRequest请求的包裹， 比如允许开发者通过HttpServletRequest的getRemoteUser()方法获得SSO登录用户的登录名，可选配置。
     * @return
     */
    @Bean
    public FilterRegistrationBean filterWrapperRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy(new HttpServletRequestWrapperFilter()));
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        registration.setName("wrapperFilter");
        // 设定加载的顺序
        registration.setOrder(5);
        return registration;
    }

    /**
     * 添加监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration(){
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(9);
        return registrationBean;
    }

    //该过滤器使得开发者可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。 比如AssertionHolder.getAssertion().getPrincipal().getName()。
    @Bean
    public FilterRegistrationBean assertionThreadLocalFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setName("assertionThreadLocalFilter");
        filterRegistrationBean.setFilter(new DelegatingFilterProxy(new AssertionThreadLocalFilter()));
        filterRegistrationBean.setOrder(6);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    //处理二次登陆 用户信息不一致
    /*@Bean
    public FilterRegistrationBean casPrincipalChangeFilterBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setName("casPrincipalChangeFilter");
        filterRegistrationBean.setFilter(new DelegatingFilterProxy(new CasPrincipalChangeFilter()));
        filterRegistrationBean.setOrder(7);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }*/

    //过滤获取用户信息 保存到session中
    @Bean
    public FilterRegistrationBean loginInfoFilterBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new LoginInfoFilter());
        filterRegistrationBean.setName("loginInfoFilter");
        filterRegistrationBean.setOrder(8);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
