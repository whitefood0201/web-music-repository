package com.whitefood.util;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Mp3Info {
    
    AudioFile audioFile;
    Tag tag;
    AudioHeader header;
    
    public Mp3Info(File file) throws Exception {
        this.audioFile = AudioFileIO.read(file);
        this.tag = this.audioFile.getTag();
        this.header = this.audioFile.getAudioHeader();
    }
    
    public int getDuration(){
        return this.header.getTrackLength();
    }
    
    public List<String> getArtists(){
        String artists = this.tag.getFirst(FieldKey.ARTIST);
        return Arrays.stream(artists.split("/")).toList();
    }
    
    public String getTitle(){
        return this.tag.getFirst(FieldKey.TITLE);
    }
    
}
