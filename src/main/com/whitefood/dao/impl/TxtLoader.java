package com.whitefood.dao.impl;

import com.whitefood.bean.Music;
import com.whitefood.listener.AppContextListener;

import javax.xml.stream.FactoryConfigurationError;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author: whitefood
 * Create on: 09-03-2023
 * <p>
 * 加载以txt格式存储的music数据库
 */
public class TxtLoader {
    
    private static TxtLoader instance;
    
    /**
     * 单例模式
     */
    public static TxtLoader getTxtLoader() {
        if (instance == null)
            instance = new TxtLoader();
        return instance;
    }
    
    public static boolean isClassLoaded(){
        return instance != null;
    }
    
    private final String dbFile = AppContextListener.getServletContext().getInitParameter("dbFile");
    private List<Music> musicList;
    private int idMax;
    
    private TxtLoader() {
        try (InputStream stream = AppContextListener.getServletContext().getResourceAsStream(this.dbFile)) {
            if (stream == null) {
                System.out.println("db file not found! Created a new one");
                this.musicList = new ArrayList<>();
                this.idMax = 0;
                return;
            }
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String max = br.readLine();
                int idMax = Integer.parseInt(max.substring(7));
                
                br.readLine(); // 数据格式定义行
                
                List<Music> musics = new ArrayList<>();
                String data;
                while ((data = br.readLine()) != null) {
                    String[] d = data.split(" \\| ");
                    
                    int mid = Integer.parseInt(d[0]);
                    String name = d[1];
                    int duration = Integer.parseInt(d[2]);
                    
                    musics.add(new Music(mid, name, duration));
                }
                
                this.idMax = idMax;
                this.musicList = musics;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * save on close
     */
    public void save() {
        String realPath = AppContextListener.getServletContext().getRealPath(this.dbFile);
        
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(realPath, false), StandardCharsets.UTF_8))) {
            writer.write("id_max=");
            writer.write(String.valueOf(this.idMax));
            writer.newLine();
            writer.write("mid | mname | duration\n");
            
            for (Music music : this.musicList) {
                writer.write(String.valueOf(music.getMid()));
                writer.write(" | ");
                writer.write(music.getName());
                writer.write(" | ");
                writer.write(String.valueOf(music.getDuration()));
                writer.newLine();
            }
            
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Music> getMusicList() {
        return musicList;
    }
    
    public int getIdMax() {
        return ++idMax;
    }
}
