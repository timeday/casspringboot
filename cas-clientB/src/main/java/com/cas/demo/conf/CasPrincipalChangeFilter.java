package com.cas.demo.conf;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

/**
 * CAS认证主体变化过滤器，用于解决二次登录造成业务端出现两个用户情况，处理方式为：检查TGC值，如果发生变更，则注销SESSION，并在当前页重定向(刷新页面)
 */
public class CasPrincipalChangeFilter implements Filter {

    /** TGC SESSION Key */
    private static final String CASTGC_SESSION_KEY = "__CASTGC__" ;

    /** 可选配置，重定向URL，如果配置该项，则跳转时，以此URL为准 */
    private String redirectUrl ;
    /** 可选配置，用于设置过滤器排除URL处理器 */
    private ExcludeUrlPatternHandler excludeUrlPatternHandler = new ExcludeUrlPatternHandler() ;

    @Override
    public void init(FilterConfig cfg) throws ServletException {
        if(this.getRedirectUrl() == null) {
            String _redirectUrl = cfg.getInitParameter("redirectUrl") ;
            if(_redirectUrl != null) this.setRedirectUrl(_redirectUrl);
        }
        // 设置排除处理器
        if(this.getExcludeUrlPatternHandler() == null) {
            String handlerClassName = cfg.getInitParameter("excludeUrlPatternHandler") ;
            if(handlerClassName != null) {
                try {
                    Class<?> type = Class.forName(handlerClassName) ;
                    Object instance = type.newInstance() ;
                    if(instance != null && instance instanceof ExcludeUrlPatternHandler) {
                        this.setExcludeUrlPatternHandler((ExcludeUrlPatternHandler) instance);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request ;
        HttpServletResponse resp = (HttpServletResponse) response ;

        // 检查请求是否需要排除
        if(this.getExcludeUrlPatternHandler() != null) {
            // 如果URL符合排除规则，直接跳过该过滤器
            if(this.getExcludeUrlPatternHandler().isExclude(req.getRequestURL().toString())) {
                chain.doFilter(request, response);
                return ;
            }
        }

        // 如果Cookie中不包含TGC信息，直接下行
        Cookie cookie = WebUtils.getCookie(req, "CASTGC") ;
        if(cookie == null || StringUtils.isBlank(cookie.getValue())) {
            chain.doFilter(request, response);
            return ;
        }

        HttpSession session = req.getSession() ;
        // 如果Session中不包含TGC信息，使用Cookie值覆盖之
        Object castgc = session.getAttribute(CASTGC_SESSION_KEY) ;
        if(castgc == null) {
            castgc = cookie.getValue() ;
            session.setAttribute(CASTGC_SESSION_KEY, castgc);
        }

        // 如果Cookie中的TGC与Session中的TGC信息不一致，说明发生了认证用户迁移
        // 注销SESSION，并重定向到当前请求
        if(!StringUtils.equals(castgc.toString(), cookie.getValue())) {
            handleChangePrincipal(req ,resp ,session) ;
            return ;
        }

        chain.doFilter(request, response);
    }

    /**
     * 处理认证主体变更逻辑(业务端可重写之，但不建议这么做)
     * @param req
     * @param resp
     * @param session
     * @throws IOException
     */
    protected void handleChangePrincipal(HttpServletRequest req
            ,HttpServletResponse resp ,HttpSession session) throws IOException {
        session.invalidate();
        if(this.getRedirectUrl() == null) {
            resp.sendRedirect(defaultRedirectUrl(req));
        } else {
            resp.sendRedirect(this.getRedirectUrl());
        }
    }

    /**
     * 默认重定向地址
     * @param req
     * @throws IOException
     */
    private String defaultRedirectUrl(HttpServletRequest req) {
        String queryString = req.getQueryString() ;
        StringBuffer sb = req.getRequestURL() ;
        if(queryString != null) {
            sb.append("?").append(queryString) ;
        }
        return sb.toString() ;
    }

    @Override
    public void destroy() {

    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public ExcludeUrlPatternHandler getExcludeUrlPatternHandler() {
        return excludeUrlPatternHandler;
    }

    public void setExcludeUrlPatternHandler(ExcludeUrlPatternHandler excludeUrlPatternHandler) {
        this.excludeUrlPatternHandler = excludeUrlPatternHandler;
    }

}
