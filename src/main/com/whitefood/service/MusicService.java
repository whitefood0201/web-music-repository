package com.whitefood.service;

import com.whitefood.bean.Music;
import com.whitefood.dao.MusicDao;
import com.whitefood.dao.impl.TxtMusicDao;
import com.whitefood.listener.AppContextListener;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class MusicService {
    
    private static final Map<String, String> DAO_MAPPING = new HashMap<>();
    
    // dao mapping, 为了 xml 中配置方便
    static {
        DAO_MAPPING.put("txt", "com.whitefood.dao.impl.TxtMusicDao");
        DAO_MAPPING.put("db", "com.whitefood.dao.impl.DBMusicDao");
    }
    
    protected MusicDao dao;
    
    public MusicService() {
        try {
            String clazz = DAO_MAPPING.get(AppContextListener.getServletContext().getInitParameter("dao"));
            this.dao = (MusicDao) Class.forName(clazz).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            AppContextListener.getServletContext().log("while initialization dao: ", e);
            this.dao = new TxtMusicDao();
        }
    }
    
    public final List<Music> selectBy(String param, SelectField field){
        if(field == SelectField.All || param == null || param.isEmpty()){
            return this.selectAll();
        } else if (field == SelectField.Name){
            return this.selectByName(param);
        } else if (field == SelectField.Mid){
            
            try {
                int mid = Integer.parseInt(param);
                return this.selectById(mid);
            } catch (NumberFormatException e){
                return Collections.emptyList();
            }
            
        } else if (field == SelectField.Artists){
            return this.selectByArtist(param);
        } else if (field == SelectField.Type){
            return this.selectByType(!param.startsWith(".") ? "."+param : param);
        }
        
        
        throw new IllegalArgumentException("Unsupported Field");
    }
    
    protected abstract List<Music> selectById(int mid);
    
    protected abstract List<Music> selectByName(String name);
    
    protected abstract List<Music> selectByArtist(String artist);
    
    protected abstract List<Music> selectByType(String type);
    
    protected abstract List<Music> selectAll();
    
    /**
     * @param tempFile saved music file, a temp file, with extension.
     * @param fn save name, without extension.
     * @return the mid of inserted music.
     */
    public abstract int add(File tempFile, String fn);
    
    public abstract boolean delete(int mid);
    
    public enum SelectField {
        Mid, Name, Artists, Type,
        All;
    }
}
