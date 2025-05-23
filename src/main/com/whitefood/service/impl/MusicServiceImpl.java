package com.whitefood.service.impl;

import com.whitefood.bean.Music;
import com.whitefood.listener.AppContextListener;
import com.whitefood.service.MusicService;
import com.whitefood.util.FileUtil;
import com.whitefood.util.Mp3Info;
import jakarta.servlet.ServletContext;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class MusicServiceImpl extends MusicService {
    
    @Override
    protected List<Music> selectById(int mid) {
        Music music = new Music();
        music.setMid(mid);
        return this.dao.select(music);
    }
    
    @Override
    protected List<Music> selectByName(String name) {
        Music music = new Music();
        music.setName(name);
        return this.dao.select(music);
    }
    
    public List<Music> selectAll(){
        return this.dao.select(null);
    }
    
    @Override
    protected List<Music> selectByArtist(String artist) {
        Music music = new Music();
        music.setArtists(artist);
        return this.dao.select(music);
    }
    
    @Override
    public int add(File file) {
        Mp3Info mp3Info = null;
        try {
            mp3Info = new Mp3Info(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mp3Info == null) return -1;
        
        int duration = mp3Info.getDuration();
        String fileName = mp3Info.getTitle().isEmpty() ?
                FileUtil.getFileName(file) : mp3Info.getTitle();
        List<String> artists = mp3Info.getArtists();
        
        Music music = new Music();
        music.setDuration(duration);
        music.setName(fileName);
        music.setArtists(artists);
        
        // 防重
        List<Music> all = this.selectAll();
        Optional<Music> first = all.stream().filter(m -> m.equals(music)).findFirst();
        return first.map(Music::getMid).orElseGet(() -> {
            File dest = new File(file.getParentFile(), fileName + FileUtil.getExt(file));
            
            boolean flg = true;
            if (!dest.exists()){
                flg = file.renameTo(dest);
            }
            
            return flg ? this.dao.insert(music) : -1;
        });
    }
    
    @Override
    public boolean delete(int mid) {
        Music music = new Music();
        music.setMid(mid);
        
        ServletContext context = AppContextListener.getServletContext();
        
        // delete file
        String name = null;
        Optional<Music> first = this.selectById(mid).stream().findFirst();
        if (first.isPresent()){
            name = first.get().getName();
        }
        String path = context.getInitParameter("staticPath");
        
        String filepath = (path.endsWith(File.separator) ? path : path + File.separator) + name + ".mp3";
        File file = new File(context.getRealPath(filepath));
        boolean flg = FileUtil.deleteFile(file);
        
        
        return this.dao.delete(music) && flg;
    }
}
