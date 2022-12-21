package com.voup.education.filter;

/**
 * @Author HIM198
 * @Date 2022 18:09
 * @Description
 **/
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CORSFilter", urlPatterns = "/*")
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;// 子承父业,多一些http协议相关的方法
        System.out.println("跨域开始");
        if (request.getHeader("Origin") != null) {// 跨域filter一定要在其他拦截器或者过滤器前面
            System.out.println("跨域开始工作");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers",
                    "Content-Type, Access-Control-Allow-Headers,Access-Token,Appid,Secret,x-ijt, Authorization, X-Requested-With");
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

}
