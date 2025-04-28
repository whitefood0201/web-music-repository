package com.whitefood.bean;

import java.util.Objects;

public class Music {
    
    private int mid ;
    private String name;
    private int duration; // in second
    
    public Music() {}
    
    public Music(int mid, String name, int duration) {
        this.mid = mid;
        this.name = name;
        this.duration = duration;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return duration == music.duration && Objects.equals(name, music.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, mid, duration);
    }
    
    /**
     * json format
     */
    @Override
    public String toString() {
        return "{ \"mid\": %d, \"name\": \"%s.mp3\", \"duration\": %d }".formatted(this.mid, this.name, this.duration);
    }
    
    /**
     * Empty in sense of select, it means no mid no name.
     * @return
     */
    public boolean isEmpty() {
        return (this.name == null || this.name.isEmpty())
                && this.mid == 0;
        
    }
}
