package com.whitefood.util;

import com.whitefood.listener.AppContextListener;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Stack;

/**
 * @author: whitefood
 * Create on: 09-03-2023
 * <p>
 * JDBC工具类，简化JDBC编程
 */
public class ConnectionPool {
    
    public static final int MAX_CONNECTIONS = 5;
    
    private static ConnectionPool instance;
    
    public static ConnectionPool getConnectionPool() {
        if (instance == null)
            instance = new ConnectionPool();
        return instance;
    }
    
    public String url;
    public String user;
    public String password;
    public String driver;
    private Stack<Connection> connectionsPool = new Stack<>();
    
    private ConnectionPool() {
        ServletContext context = AppContextListener.getServletContext();
        this.url = context.getInitParameter("url");
        this.user = context.getInitParameter("user");
        this.password = context.getInitParameter("password");
        this.driver = context.getInitParameter("driver");
        try{
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取连接
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        if (this.connectionsPool.isEmpty())
            this.connectionsPool.push(DriverManager.getConnection(url, user, password));
        return this.connectionsPool.pop();
    }
    
    /**
     * 释放资源
     *
     * @param conn 连接对象
     * @param stmt 操作对象
     * @param rs   结果集
     */
    public void close(Connection conn, Statement stmt, ResultSet rs) {
        
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        
        if (conn != null) {
            try {
                if (this.connectionsPool.size() >= MAX_CONNECTIONS)
                    conn.close();
                else
                    this.connectionsPool.push(conn);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
