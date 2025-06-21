package com.javali.hackathon.bsbdfappbackendhackathon.utils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class AudioConverter {

    public static File convertToWavMono16KHz(File inputFile) throws Exception {
        AudioInputStream originalStream = AudioSystem.getAudioInputStream(inputFile);

        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                16000,
                16,
                1,
                2,
                16000,
                false
        );

        AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, originalStream);

        File outputFile = File.createTempFile("converted", ".wav");
        AudioSystem.write(convertedStream, AudioFileFormat.Type.WAVE, outputFile);

        originalStream.close();
        convertedStream.close();

        return outputFile;
    }
}
