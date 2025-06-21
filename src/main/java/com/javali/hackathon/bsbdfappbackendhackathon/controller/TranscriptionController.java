package com.javali.hackathon.bsbdfappbackendhackathon.controller;

import com.javali.hackathon.bsbdfappbackendhackathon.service.TranscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    @PostMapping("/{pessoaBase}/transcribe")
    public ResponseEntity<String> transcribe(@PathVariable String pessoaBase, @RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("O arquivo de áudio não pode ser vazio.");
        }

        try {
            String result = transcriptionService.transcribe(file);
            //TODO Salvar a transcrição no banco de dados.
            result = pessoaBase + " envio o audio com a seguinte mensagem: " + result;
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Erro ao transcrever o áudio para o usuário '{}': {}", pessoaBase, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao processar o áudio.");
        }
    }
}