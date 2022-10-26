package com.dubbo.simulate.framework.protocol.http;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Date: 2022/10/24
 */
public class MyDispatcherServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理一个请求，可以不同hanler实现
        new HttpServletHandler().handler(req, resp);
    }

    //@Override
    //protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //    doPost(req,resp);
    //}
    //
    //@Override
    //protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //    resp.getWriter().write("Hello MyServlet");
    //}
}
