package com.javali.hackathon.bsbdfappbackendhackathon.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javali.hackathon.bsbdfappbackendhackathon.model.UserContacts;
import com.javali.hackathon.bsbdfappbackendhackathon.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final ContactRepository contactRepository;
    private final ObjectMapper objectMapper; // Injeta o ObjectMapper gerenciado pelo Spring

    @Override
    public void run(ApplicationArguments args) {
        log.info("Iniciando o carregamento de dados a partir do arquivo JSON...");

        try (InputStream inputStream = getClass().getResourceAsStream("/mock/contatos.json")) {

            if (inputStream == null) {
                log.error("Arquivo de dados '/mock/contatos.json' não encontrado no classpath. A aplicação continuará com a lista de contatos vazia.");
                contactRepository.setInitialData(Collections.emptyList());
                return;
            }

           TypeReference<List<UserContacts>> typeReference = new TypeReference<>() {};
            List<UserContacts> loadedContacts = objectMapper.readValue(inputStream, typeReference);

            contactRepository.setInitialData(loadedContacts);

        } catch (Exception e) {
            log.error("Ocorreu um erro crítico ao carregar os dados de contatos. A aplicação pode não funcionar como esperado.", e);
        }
    }
}