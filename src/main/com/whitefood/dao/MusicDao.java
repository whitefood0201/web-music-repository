package com.whitefood.dao;

import com.whitefood.bean.Music;

import java.util.List;

public interface MusicDao {
    
    /**
     * Select first with mid, if no have mid, with mname.
     * If music is empty or null, select all.
     * @param music
     * @return select result, return null if have any exception or other case.
     */
    List<Music> select(Music music);
    
    /**
     * @param music
     * @return the mid of inserted music
     */
    int insert(Music music);
    
    boolean delete(Music music);
    
}
