package com.whitefood.bean;

import java.util.*;
import java.util.stream.Collectors;

public class Music {
    
    private int mid = -1;
    private String name;
    private int duration; // in second
    private List<String> artists;
    private String type;
    
    public Music() {}
    
    public Music(int mid, String name, int duration) {
        this.mid = mid;
        this.name = name;
        this.duration = duration;
    }
    
    public Music(int mid, String name, int duration, List<String> artists, String type) {
        this.mid = mid;
        this.name = name;
        this.duration = duration;
        this.artists = artists;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public int getMid() {
        return mid;
    }
    
    public void setMid(int mid) {
        this.mid = mid;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public List<String> getArtists() {
        return artists;
    }
    
    public void setArtists(String... artists) {
        this.artists = Arrays.stream(artists).toList();
    }
    
    public void setArtists(List<String> artists) {
        this.artists = artists;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return duration == music.duration
               && Objects.equals(name, music.name)
               && Objects.equals(artists, music.artists)
               && Objects.equals(type, music.type);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, mid, duration);
    }
    
    
    /**
     * json format
     */
    public static final String STR_JSON =
            "{ \"mid\": %d, \"name\": \"%s%s\", \"duration\": %d, \"artists\": %s}";
    @Override
    public String toString() {
        String artistsJsonArray = "[]";
        if (this.artists != null){
            artistsJsonArray = this.artists.stream()
                .map(s -> '"' + s + '"')  // 给每个元素加引号
                .collect(Collectors.joining(", ", "[", "]"));
        }
        
        return STR_JSON.formatted(this.mid, this.name, this.type, this.duration, artistsJsonArray);
    }
    
    /**
     * Empty in sense of select, it means no mid ni name ni artists.
     * @return
     */
    public boolean isEmpty() {
        return (this.name == null || this.name.isEmpty())
                && (this.type == null || this.type.isEmpty())
                && this.mid == -1
                && (this.getArtists() == null
                    || this.getArtists().isEmpty()
                    || this.getArtists().get(0).isEmpty());
    }
}
