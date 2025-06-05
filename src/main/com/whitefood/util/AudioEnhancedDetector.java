package com.whitefood.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * thanks chatgpt
 */
public class AudioEnhancedDetector {
    
    public enum FileType {
        ID3_TAG,
        RAW_MPEG_STREAM,
        FLAC,
        INVALID
    }
    
    public FileType detectFileType(InputStream inputStream) {
        if (inputStream == null) {
            return FileType.INVALID;
        }
        
        try {
            inputStream.mark(10); // 标记当前流位置
            
            byte[] header = new byte[4];
            int bytesRead = inputStream.read(header);
            if (bytesRead < 4) {
                return FileType.INVALID;
            }
            
            String headStr = new String(header, 0, 4, "ISO-8859-1");
            
            if ("ID3".equals(headStr.substring(0, 3))) {
                return FileType.ID3_TAG;
            } else if ("fLaC".equals(headStr)) {
                return FileType.FLAC;
            } else {
                boolean isSync = (header[0] & 0xFF) == 0xFF &&
                        (header[1] & 0xE0) == 0xE0;
                if (isSync) {
                    return FileType.RAW_MPEG_STREAM;
                } else {
                    return FileType.INVALID;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return FileType.INVALID;
        } finally {
            try {
                inputStream.reset(); // 恢复到标记位置
            } catch (IOException e) {
                // 忽略 reset 异常
            }
        }
    }
}