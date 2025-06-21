package com.javali.hackathon.bsbdfappbackendhackathon.service;

import com.javali.hackathon.bsbdfappbackendhackathon.utils.AudioConverter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TranscriptionService {

    @Value("${vosk.model.path}")
    private String modelPathValue;
    private final ResourceLoader resourceLoader;
    private Model model;

    @PostConstruct
    public void init() {
        LibVosk.setLogLevel(LogLevel.WARNINGS);
        log.info("Tentando carregar o modelo Vosk a partir do caminho do classpath: {}", modelPathValue);

        try {
            Resource modelResource = resourceLoader.getResource(modelPathValue);

            File modelDir = modelResource.getFile();
            this.model = new Model(modelDir.getAbsolutePath());
            log.info("Modelo Vosk carregado com sucesso do caminho: {}", modelDir.getAbsolutePath());

        } catch (IOException e) {
            log.error("Falha ao carregar o modelo Vosk. Verifique se o caminho '{}' está correto e acessível no classpath.", modelPathValue, e);
            throw new RuntimeException("Não foi possível carregar o modelo Vosk.", e);
        }
    }

    public String transcribe(MultipartFile file) throws Exception {
        File tempFile = null;
        File wavFile = null;
        try {

            tempFile = File.createTempFile("audio_in_", ".tmp");
            file.transferTo(tempFile);

            wavFile = AudioConverter.convertToWavMono16KHz(tempFile);

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
                 Recognizer recognizer = new Recognizer(model, 16000.0f)) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioStream.read(buffer)) >= 0) {
                    recognizer.acceptWaveForm(buffer, bytesRead);
                }

                return recognizer.getFinalResult();
            }
        } finally {
            if (tempFile != null) tempFile.delete();
            if (wavFile != null) wavFile.delete();
        }
    }

    @PreDestroy
    public void onDestroy() {
        if (this.model != null) {
            this.model.close();
        }
    }
}