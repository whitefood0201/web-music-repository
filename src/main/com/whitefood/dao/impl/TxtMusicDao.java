package com.whitefood.dao.impl;

import com.whitefood.bean.Music;
import com.whitefood.dao.MusicDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TxtMusicDao implements MusicDao {
    
    private final TxtLoader loader = TxtLoader.getTxtLoader();
    
    public TxtMusicDao() {}
    
    @Override
    public List<Music> select(Music music) {
        if (music == null || music.isEmpty() || music.getMid() == 0){
            return this.loader.getMusicList().stream().toList();
        } else if (music.getMid() > 0){
            return this.loader.getMusicList().stream().filter(m -> m.getMid() == music.getMid()).toList();
        } else if (music.getName() != null){
            return this.loader.getMusicList().stream().filter(m -> m.getName().contains(music.getName())).toList();
        } else if (music.getArtists() != null){
            // 作曲家list 是否存在交集
            return this.loader.getMusicList().stream().filter(m -> !Collections.disjoint(m.getArtists(), music.getArtists())).toList();
        }
        return new ArrayList<>();
    }
    
    @Override
    public int insert(Music music) {
        music.setMid(this.loader.getIdMax());
        this.loader.getMusicList().add(music);
        return music.getMid();
    }
    
    @Override
    public boolean delete(Music music) {
        return this.loader.getMusicList().removeIf(m -> m.getMid() == music.getMid());
    }
    
}
