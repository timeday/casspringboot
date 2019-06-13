package com.cas.demo.conf;

import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CrosFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 解决跨域问题
        String origin = request.getHeader("origin");
        System.out.println("=======CrosFilter--origin：" + origin);
        if (StringUtils.isEmpty(origin) || "null".equals(origin)) {
            origin = "http://www.server.com:8443/cas/login;";
        }
        response.addHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Origin", origin);
        response.addHeader("Allow-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
