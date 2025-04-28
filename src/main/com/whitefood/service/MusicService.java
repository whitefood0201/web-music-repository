package com.whitefood.service;

import com.whitefood.bean.Music;
import com.whitefood.dao.MusicDao;

import java.io.File;
import java.util.List;

public interface MusicService {
    
    List<Music> selectBy(String param, SelectWay way);
    
    List<Music> selectById(int mid);
    
    List<Music> selectByName(String name);
    
    List<Music> selectAll();
    
    /**
     * @param file saved music file
     * @return the mid of inserted music
     */
    int add(File file);
    
    boolean delete(int mid);
    
    enum SelectWay{
        byName, byMid;
    }
}
