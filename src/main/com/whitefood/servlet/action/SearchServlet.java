package com.whitefood.servlet.action;

import com.whitefood.bean.Music;
import com.whitefood.service.MusicService;
import com.whitefood.service.impl.MusicServiceImpl;
import com.whitefood.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet("/action/search")
public class SearchServlet extends HttpServlet {
    
    MusicService service = new MusicServiceImpl();
    
    private String location;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.location = FileUtil.folderPathStd(this.getServletContext().getInitParameter("staticPath"));
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchText = req.getParameter("text");
        int searchField = Integer.parseInt(Optional
                .ofNullable(req.getParameter("searchField"))
                .orElse(String.valueOf(MusicService.SelectField.values().length - 1))); // All
        
        if (searchField >= MusicService.SelectField.values().length) throw new IllegalArgumentException("Unsupported Field");
        
        MusicService.SelectField field = MusicService.SelectField.values()[searchField];
        List<Music> musics = service.selectBy(searchText, field);
        
        resp.setContentType("text/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        PrintWriter out = resp.getWriter();
        
        out.println("{");
        out.println("\"response\": 200,");
        out.println("\"path\": \"%s\",".formatted(this.location));
        out.println("\"datas\":" + musics);
        out.println("}");
        
        out.flush();
        out.close();
    }
    
}
