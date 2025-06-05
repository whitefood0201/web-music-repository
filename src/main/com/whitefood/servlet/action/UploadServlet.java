package com.whitefood.servlet.action;

import com.whitefood.service.MusicService;
import com.whitefood.service.impl.MusicServiceImpl;
import com.whitefood.util.AudioEnhancedDetector;
import com.whitefood.util.FileUtil;
import com.whitefood.util.EncodingDecoder;
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
@MultipartConfig
public class UploadServlet extends HttpServlet {
    
    private MusicService service = new MusicServiceImpl();
    
    // 上传配置
    private String savePath;
    private int maxFileSize; // in MB
    
    @Override
    public void init() throws ServletException {
        super.init();
        String staticPath = this.getServletContext().getInitParameter("staticPath");
        String realPath = this.getServletContext().getRealPath(staticPath);
        this.savePath = FileUtil.folderPathStd(realPath, File.separator);
        
        FileUtil.createIfNotExists(new File(this.savePath));
        
        this.maxFileSize = Integer.parseInt(this.getServletContext().getInitParameter("maxFileSize"));
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part = req.getPart("file");
        
        // 判断文件大小
        if (part.getSize() > (long) this.maxFileSize * 1024 * 1024){
            resp.sendRedirect(this.getServletContext().getContextPath() + "/error.html?msg="
                    + EncodingDecoder.encodingUTF8("文件过大，应小于 %d MB".formatted(this.maxFileSize)));
            return;
        }
        
        // 检查格式
        try (InputStream fis = part.getInputStream()){
            AudioEnhancedDetector detector = new AudioEnhancedDetector();
            AudioEnhancedDetector.FileType type = detector.detectFileType(fis);
            if (type == AudioEnhancedDetector.FileType.INVALID){
                resp.sendRedirect(this.getServletContext().getContextPath() + "/error.html?msg=" + EncodingDecoder.encodingUTF8("不支持的文件格式"));
                return;
            }
        }
        
        
        String fileName = Path.of(part.getSubmittedFileName()).getFileName().toString();
        
        // 保存为 temp 文件，后续写入数据库后在改名
        String filepath = this.savePath + "temp" + FileUtil.getExt(fileName);
        boolean isSuccess = writeFile(part, filepath);
        
        if (isSuccess) {
            // 存入数据库
            int mid = this.service.add(new File(filepath), FileUtil.getFileName(fileName));
            if (mid != -1){
                resp.sendRedirect(this.getServletContext().getContextPath() + "/search.html?mid=" + mid);
                return;
            }
        }
        resp.sendRedirect(this.getServletContext().getContextPath() + "/error.html?msg=" + EncodingDecoder.encodingUTF8("上传失败"));
        
    }
    
    /**
     *
     * @param part
     * @return isSuccess
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
