package com.whitefood.util;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

import java.io.File;

/**
 * thinks chatgpt
 */
public class MusicUtil {
    public static int getDuration(File file){
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            AudioHeader audioHeader = audioFile.getAudioHeader();
            return audioHeader.getTrackLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
