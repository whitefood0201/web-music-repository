package com.whitefood.util;

import java.awt.*;
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
        return getFileName(file.getName());
    }
    public static String getFileName(String fileName){
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileName = fileName.substring(0, dotIndex);
        }
        return fileName;
    }
    
    public static String getExt(File file){
        return getExt(file.getName());
    }
    public static String getExt(String fileName){
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileName = fileName.substring(dotIndex);
        }
        return fileName;
    }
    
    
    public static String folderPathStd(String folder) {
        return folderPathStd(folder, "/");
    }
    
    /**
     * @param folder
     * @param separator
     * @return "folderName<separotor>"
     */
    public static String folderPathStd(String folder, String separator){
        if (folder == null) return separator;
        String s = folder;
        if (folder.startsWith("/") || folder.startsWith("\\")){
            s = s.substring(1);
        }
        if (folder.endsWith("/") || folder.endsWith("\\")){
            s = s.substring(0, folder.length()-1);
        }
        return s + separator;
    }
    
}
