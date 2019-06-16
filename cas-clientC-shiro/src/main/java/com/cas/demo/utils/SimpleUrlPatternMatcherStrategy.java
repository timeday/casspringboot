package com.cas.demo.utils;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

import javax.servlet.annotation.WebFilter;
import java.util.regex.Pattern;

/**
 * 描述：
 *      url匹配策略
 **/
@WebFilter(urlPatterns = "/*")
public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {

    private Pattern pattern;
    /**
     * 过滤url
     * @param url
     * @return
     */
    @Override
    public boolean matches(String url) {

       /* //在此可以做额外的扩展,比如判断是insert也放行 可以通过查询数据库来进行动态判断
        if(url.contains("/insert")){
            return true;
        }

        //默认是根据cas.ignore-pattern来判断是否否满足过滤
        return this.pattern.matcher(url).find();*/


        //过滤的url
        return url.contains("/logout/success");
    }

    /**
     * 正则表达式过滤方法
     * @param pattern
     */
    @Override
    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }
}
