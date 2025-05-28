package com.whitefood.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * thanks chatgpt
 */
public class EncodingDecoder {
    
    private EncodingDecoder() {
    }
    
    public static String decodeHtmlEntities(String input) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            if (input.startsWith("&#x", i)) {
                int semicolon = input.indexOf(';', i);
                if (semicolon > i) {
                    String hexCode = input.substring(i + 3, semicolon);
                    try {
                        int charCode = Integer.parseInt(hexCode, 16); // 16进制转10进制
                        result.append((char) charCode); // 转为字符
                        i = semicolon + 1;
                        continue;
                    } catch (NumberFormatException e) {
                        // 无效的十六进制编码，跳过
                    }
                }
            }
            result.append(input.charAt(i));
            i++;
        }
        return result.toString();
    }
    
    public static String encodingUTF8(String input) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }
}