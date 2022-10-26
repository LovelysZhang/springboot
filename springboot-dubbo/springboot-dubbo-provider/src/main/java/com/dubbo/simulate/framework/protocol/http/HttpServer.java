package com.dubbo.simulate.framework.protocol.http;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @Date: 2022/10/24
 */
public class HttpServer {

    public void start(String hostname, int port) {
        // Tomcat、Jetty，具体看用户配置在哪里。mysql、zookeeper、redis
        // 假设用户配置k:v，tomcat

        //使用内嵌的Tomcat
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(port);

        StandardEngine engine = new StandardEngine();
        engine.setDefaultHost(hostname);

        StandardHost host = new StandardHost();
        host.setName(hostname);

        String contextPath = "";
        StandardContext context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        SecurityConstraint securityConstraint = new SecurityConstraint();
        SecurityCollection collection = new SecurityCollection();
        collection.addPattern("/*");
        collection.addMethod("HEAD");
        collection.addMethod("POST");
        collection.addMethod("GET");
        collection.addMethod("PUT");
        collection.addMethod("DELETE");
        collection.addMethod("OPTIONS");
        collection.addMethod("TRACE");
        collection.addMethod("COPY");
        collection.addMethod("SEARCH");
        collection.addMethod("PROPFIND");
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);

        //tomcat中将servlet与mapping绑定
        tomcat.addServlet(contextPath, "dispatcher", new MyDispatcherServlet());
        context.addServletMappingDecoded("/*", "dispatcher");

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
