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

@WebServlet("/action/m3u8")
public class M3u8Servlet extends HttpServlet {
    
    private MusicService service = new MusicServiceImpl();
    
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
        String host = req.getParameter("host");
        String searchText = req.getParameter("text");
        int searchField = Integer.parseInt(Optional
                .ofNullable(req.getParameter("searchField"))
                .orElse(String.valueOf(MusicService.SelectField.values().length - 1))); // All
        
        if (searchField > 3) throw new IllegalArgumentException("Unsupported Field");
        
        MusicService.SelectField field = MusicService.SelectField.values()[searchField];
        this.getServletConfig().getInitParameter("staticPath");
        List<Music> musics = service.selectBy(searchText, field);
        
        String s = musics.isEmpty() ? "No Data" :
                String.join("\n", musics.stream().map(m -> {
                    // 拼接绝对路径
                    return FileUtil.folderPathStd(host)
                            + FileUtil.folderPathStd(this.getServletContext().getContextPath())
                            + this.location
                            + m.getName() + m.getType();
                }).toList());
        
        resp.setContentType("application/vnd.apple.mpegurl");
        resp.setCharacterEncoding("UTF-8");
        
        PrintWriter out = resp.getWriter();
        
        out.println("#EXTM3U8");
        out.println(s);
        
        out.flush();
        out.close();
    }
}
