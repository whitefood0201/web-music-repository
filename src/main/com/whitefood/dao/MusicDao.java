package com.whitefood.dao;

import com.whitefood.bean.Music;

import java.util.Collections;
import java.util.List;

public abstract class MusicDao {
    
    /**
     * First select with mid, then name, then artists.
     * If music is empty or null, select all.
     * @param music music obj with select param
     * @return select result, return empty list if have any exception or other case.
     */
    public List<Music> select(Music music) {
        if (music == null || music.isEmpty() || music.getMid() == 0){
            return this.selectAll();
        } else if (music.getMid() > 0){
            return this.selectById(music);
        } else if (music.getName() != null){
            return this.selectByName(music);
        } else if (music.getType() != null) {
            return this.selectByType(music);
        } else if (music.getArtists() != null || !music.getArtists().isEmpty()) {
            return this.selectByArtist(music);
        }
        return Collections.emptyList();
    }
    
    protected abstract List<Music> selectById(Music music);
    
    protected abstract List<Music> selectByName(Music music);
    
    protected abstract List<Music> selectByArtist(Music music);
    
    protected abstract List<Music> selectByType(Music music);
    
    protected abstract List<Music> selectAll();
    
    /**
     * @param music
     * @return the mid of inserted music
     */
    public abstract int insert(Music music);
    
    /**
     * delete using MID
     * @param music
     * @return
     */
    public abstract boolean delete(Music music);
    
}
