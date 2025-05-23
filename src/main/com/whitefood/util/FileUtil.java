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
    
    public static String getFileName(File file){
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileName = fileName.substring(0, dotIndex);
        }
        return fileName;
    }
    
    public static String getExt(File file){
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileName = fileName.substring(dotIndex);
        }
        return fileName;
    }
    
    public static String getExt(String fileName){
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileName = fileName.substring(dotIndex);
        }
        return fileName;
    }
    
}
