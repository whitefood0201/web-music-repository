package com.whitefood.service.impl;

import com.whitefood.bean.Music;
import com.whitefood.dao.MusicDao;
import com.whitefood.dao.impl.TxtMusicDao;
import com.whitefood.listener.AppContextListener;
import com.whitefood.service.MusicService;
import com.whitefood.util.FileUtil;
import com.whitefood.util.MusicUtil;
import jakarta.servlet.ServletContext;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MusicServiceImpl implements MusicService {
    
    private static final Map<String, String> DAO_MAPPING = new HashMap<>();
    
    // dao mapping, 为了 xml 中配置方便
    static {
        DAO_MAPPING.put("txt", "com.whitefood.dao.impl.TxtMusicDao");
        DAO_MAPPING.put("db", "com.whitefood.dao.impl.DBMusicDao");
    }
    
    private MusicDao dao;
    
    public MusicServiceImpl() {
        try {
            String clazz = DAO_MAPPING.get(AppContextListener.getServletContext().getInitParameter("dao"));
            this.dao = (MusicDao) Class.forName(clazz).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            this.dao = new TxtMusicDao();
        }
    }
    
    @Override
    public List<Music> selectBy(String param, SelectWay way) {
        if(param.isEmpty()){
            return this.selectAll();
        } else if (way == SelectWay.byName){
            return this.selectByName(param);
        } else {
            return this.selectById(Integer.parseInt(param));
        }
    }
    
    @Override
    public List<Music> selectById(int mid) {
        Music music = new Music();
        music.setMid(mid);
        return this.dao.select(music);
    }
    
    @Override
    public List<Music> selectByName(String name) {
        Music music = new Music();
        music.setName(name);
        return this.dao.select(music);
    }
    
    public List<Music> selectAll(){
        return this.dao.select(null);
    }
    
    @Override
    public int add(File file) {
        int duration = MusicUtil.getDuration(file);
        
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex);
        }
        
        Music music = new Music();
        music.setDuration(duration);
        music.setName(fileName);
        
        // 防重
        List<Music> all = this.selectAll();
        Optional<Music> first = all.stream().filter(m -> m.equals(music)).findFirst();
        return first.map(Music::getMid).orElseGet(() -> this.dao.insert(music));
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
