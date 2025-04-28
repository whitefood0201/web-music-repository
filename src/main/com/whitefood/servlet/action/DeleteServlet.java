package com.whitefood.servlet.action;

import com.whitefood.service.MusicService;
import com.whitefood.service.impl.MusicServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/action/delete")
public class DeleteServlet extends HttpServlet {
    
    MusicService service = new MusicServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String pwd = req.getParameter("pwd");
        int mid = Integer.parseInt(req.getParameter("mid"));
        
        PrintWriter writer = resp.getWriter();
        
        if (this.getServletContext().getInitParameter("canDelete").equalsIgnoreCase("true") && this.getServletContext().getInitParameter("username").equals(username)
                && this.getServletContext().getInitParameter("pwd").equals(pwd)) {
            if (this.service.delete(mid)) {
                writer.println("Delete Success!");
            } else {
                writer.println("Delete Failed, or in database or in file.");
            }
        } else {
            writer.println("?");
        }
        
        writer.flush();
        writer.close();
    }
    
}
