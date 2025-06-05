package com.whitefood.dao.impl;

import com.whitefood.bean.Music;
import com.whitefood.dao.MusicDao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TxtMusicDao extends MusicDao {
    
    private final TxtLoader loader = TxtLoader.getTxtLoader();
    
    public TxtMusicDao() {}
    
    @Override
    protected List<Music> selectAll() {
        return this.loader.getMusicList().stream().toList();
    }
    
    @Override
    protected List<Music> selectByArtist(Music music) {
        return this.loader.getMusicList().stream().filter(m -> {
            String s1 = String.join("/", m.getArtists());
            String s2 = String.join("/", music.getArtists());
            return s1.contains(s2);
        }).toList();
    }
    
    @Override
    protected List<Music> selectByName(Music music) {
        return this.loader.getMusicList().stream().filter(m -> m.getName().contains(music.getName())).toList();
    }
    
    @Override
    protected List<Music> selectById(Music music) {
        return this.loader.getMusicList().stream().filter(m -> m.getMid() == music.getMid()).toList();
    }
    
    @Override
    protected List<Music> selectByType(Music music) {
        return this.loader.getMusicList().stream().filter(m -> Objects.equals(m.getType(), music.getType())).toList();
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
