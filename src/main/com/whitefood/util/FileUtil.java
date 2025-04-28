package com.whitefood.util;

import java.io.File;

public class FileUtil {
    
    private FileUtil(){}
    
    public static boolean createIfNotExists(File folder){
        if (folder.isDirectory() && !folder.exists()){
            return folder.mkdirs();
        }
        return false;
    }
    
    public static boolean deleteFile(File file){
        if (!file.exists() || !file.isFile())
            return false;
        return file.delete();
    }
    
}
