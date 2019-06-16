package com.cas.demo.conf;

import org.apache.commons.lang3.StringUtils;

/**
 * @Project springbootcasdemo
 * @Package com.oumuv.demo.conf
 * @ClassName ExcludeUrlPatternHandler
 * @Descripition 默认排除处理器(排除所有静态文件)
 **/
public class ExcludeUrlPatternHandler {

    public boolean isExclude(String uri) {
        if(uri == null) return true ;
        // 所有静态资源请求都被排除
        if(StringUtils.endsWithIgnoreCase(uri, ".js")
                || StringUtils.endsWithIgnoreCase(uri, ".d")
                || StringUtils.endsWithIgnoreCase(uri, ".png")
                || StringUtils.endsWithIgnoreCase(uri, ".jpg")
                || StringUtils.endsWithIgnoreCase(uri, ".gif")
                || StringUtils.endsWithIgnoreCase(uri, ".ico"))
            return true ;

        return false ;
    }
}
