package com.whitefood.listener;

import com.whitefood.dao.impl.TxtLoader;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static ServletContext servletContext;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        System.out.println("ServletContext initialized: " + servletContext.getContextPath());
        System.out.println("Using Dao: " + servletContext.getInitParameter("dao"));
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext destroyed: " + servletContext.getContextPath());
        if (TxtLoader.isClassLoaded())
            TxtLoader.getTxtLoader().save();
    }
    
    public static ServletContext getServletContext() {
        return servletContext;
    }
}