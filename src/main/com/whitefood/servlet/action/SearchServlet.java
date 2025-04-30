package com.whitefood.servlet.action;

import com.whitefood.bean.Music;
import com.whitefood.service.MusicService;
import com.whitefood.service.impl.MusicServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/action/search")
public class SearchServlet extends HttpServlet {
    
    MusicService service = new MusicServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchText = req.getParameter("text");
        MusicService.SelectWay way = MusicService.SelectWay.values()[Integer.parseInt(req.getParameter("way"))];
        List<Music> music = service.selectBy(searchText, way);
        
        
        resp.setContentType("text/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        PrintWriter out = resp.getWriter();
        
        out.println("{");
        out.println("\"response\": 200,");
        out.println("\"datas\":" + music);
        out.println("}");
        
        out.flush();
        out.close();
    }
    
}
