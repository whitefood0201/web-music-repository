package com.whitefood.servlet.action;

import com.whitefood.listener.AppContextListener;
import com.whitefood.service.MusicService;
import com.whitefood.service.impl.MusicServiceImpl;
import com.whitefood.util.FileUtil;
import com.whitefood.util.EncodingDecoder;
import com.whitefood.util.Mp3EnhancedDetector;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;


@WebServlet("/action/upload")
@MultipartConfig(
        fileSizeThreshold = 1024*1024 * 3, // 3MB
        maxFileSize = 1024*1024 * 15, // 15MB
        maxRequestSize = 1024*1024 * 17 // 17MB
)
public class UploadServlet extends HttpServlet {
    
    private MusicService service = new MusicServiceImpl();
    
    // 上传配置
    private String location;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.location = this.getServletContext().getRealPath(AppContextListener.getServletContext().getInitParameter("staticPath"));
        
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part = req.getPart("file");
        
        try (InputStream fis = part.getInputStream()){
            Mp3EnhancedDetector detector = new Mp3EnhancedDetector();
            Mp3EnhancedDetector.Mp3Type type = detector.detectMp3Type(fis);
            if (type == Mp3EnhancedDetector.Mp3Type.INVALID){
                resp.sendRedirect(this.getServletContext().getContextPath() + "/error.html?msg=" + EncodingDecoder.encodingUTF8("并非mp3文件"));
                return;
            }
        }
        
        String fileName = Path.of(part.getSubmittedFileName()).getFileName().toString();
        
        String savePath = this.location.endsWith(File.separator) ? this.location : this.location + File.separator;
        FileUtil.createIfNotExists(new File(savePath));
        
        String filepath = savePath+fileName;
        boolean isSuccess = writeFile(part, filepath);
        
        if (isSuccess) {
            // 存入数据库
            int mid = this.service.add(new File(filepath));
            resp.sendRedirect(this.getServletContext().getContextPath() + "/search.html?mid="+mid);
        } else {
            resp.sendRedirect(this.getServletContext().getContextPath() + "/error.html?msg=" + EncodingDecoder.encodingUTF8("上传失败"));
        }
    }
    
    /**
     *
     * @param part
     * @return msg
     */
    private boolean writeFile(Part part, String filepath){
        boolean isSuccess = false;
        try {
            part.write(filepath);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}
