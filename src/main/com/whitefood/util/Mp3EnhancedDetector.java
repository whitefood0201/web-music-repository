package com.whitefood.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * thanks chatgpt
 */
public class Mp3EnhancedDetector {
    
    public enum Mp3Type {
        ID3_TAG,
        RAW_MPEG_STREAM,
        INVALID
    }
    
    public Mp3Type detectMp3Type(InputStream inputStream) {
        if (inputStream == null) {
            return Mp3Type.INVALID;
        }
        
        try {
            inputStream.mark(10); // 标记当前流位置
            
            byte[] header = new byte[4];
            int bytesRead = inputStream.read(header);
            if (bytesRead < 3) {
                return Mp3Type.INVALID;
            }
            
            String headStr = new String(header, 0, 3, "ISO-8859-1");
            if ("ID3".equals(headStr)) {
                return Mp3Type.ID3_TAG;
            } else if (bytesRead == 4) {
                boolean isSync = (header[0] & 0xFF) == 0xFF &&
                        (header[1] & 0xE0) == 0xE0;
                if (isSync) {
                    return Mp3Type.RAW_MPEG_STREAM;
                } else {
                    return Mp3Type.INVALID;
                }
            } else {
                return Mp3Type.INVALID;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Mp3Type.INVALID;
        } finally {
            try {
                inputStream.reset(); // 恢复到标记位置
            } catch (IOException e) {
                // 如果流不支持 reset，可以忽略
            }
        }
    }
    
}
