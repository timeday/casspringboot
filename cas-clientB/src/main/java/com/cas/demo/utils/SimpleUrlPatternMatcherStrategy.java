package com.cas.demo.utils;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

import javax.servlet.annotation.WebFilter;

/**
 * 描述：
 *      url匹配策略
 **/
@WebFilter(urlPatterns = "/*")
public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {

    /**
     * 过滤url
     * @param url
     * @return
     */
    @Override
    public boolean matches(String url) {
        //过滤的url
        return url.contains("/logout/success");
    }

    /**
     * 正则表达式过滤方法
     * @param s
     */
    @Override
    public void setPattern(String s) {

    }
}
