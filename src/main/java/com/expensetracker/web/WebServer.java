package com.expensetracker.web;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.jasper.servlet.JasperInitializer;
import org.apache.jasper.runtime.JspFactoryImpl;
import javax.servlet.jsp.JspFactory;

import java.io.File;

public class WebServer {

    public static void start() {
        new Thread(() -> {
            try {
                Tomcat tomcat = new Tomcat();
                tomcat.setBaseDir("temp"); // Set explicitly to avoid permission/missing dir issues
                tomcat.setPort(8080);
                tomcat.getConnector(); // trigger connector creation

                String webappDirLocation = "src/main/webapp/";
                File docBase = new File(webappDirLocation);
                if (!docBase.exists()) {
                    docBase.mkdirs();
                }

                Context ctx = tomcat.addWebapp("/", docBase.getAbsolutePath());
                
                // Allow Jasper to access applications classes and dependencies
                ctx.setParentClassLoader(WebServer.class.getClassLoader());

                // Manually initialize the JspFactory if not present
                if (JspFactory.getDefaultFactory() == null) {
                    JspFactory.setDefaultFactory(new JspFactoryImpl());
                }
                
                // Add the Jasper Initializer inside the Tomcat Context
                ctx.addServletContainerInitializer(new JasperInitializer(), null);

                // Register Servlets
                Tomcat.addServlet(ctx, "dashboardServlet", new DashboardServlet());
                ctx.addServletMappingDecoded("/dashboard", "dashboardServlet");
                
                Tomcat.addServlet(ctx, "loginServlet", new LoginServlet());
                ctx.addServletMappingDecoded("/login", "loginServlet");
                ctx.addServletMappingDecoded("/", "loginServlet");

                System.out.println("Starting Web Server on port 8080...");
                tomcat.start();
                tomcat.getServer().await();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
