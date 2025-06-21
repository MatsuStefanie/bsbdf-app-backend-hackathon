package com.javali.hackathon.bsbdfappbackendhackathon.service;

import com.javali.hackathon.bsbdfappbackendhackathon.DTO.*;
import com.javali.hackathon.bsbdfappbackendhackathon.config.WhatsAppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WhatsAppAlertaService {

    private final WhatsAppProperties props;
    private final RestTemplate restTemplate;

    public void enviarAlerta(AlertaRequest dto) {
        log.info("Construindo e enviando alerta para o número: {}", dto.numero());

        WhatsAppMessage requestBody = buildRequestBody(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(props.getToken());

        HttpEntity<WhatsAppMessage> entity = new HttpEntity<>(requestBody, headers);


        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    props.getUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            log.info("Alerta enviado com sucesso para {}. Resposta da API: {}", dto.numero(), response.getBody());
        } catch (RestClientException e) {
            log.error("Falha ao enviar alerta para {}. Erro: {}", dto.numero(), e.getMessage());
        }
    }

    private WhatsAppMessage buildRequestBody(AlertaRequest dto) {

        var header = new Component("header", List.of(new Parameter("text", dto.nomeAlerta())), null, null);
        var body = new Component("body", List.of(
                new Parameter("text", dto.nomeAlerta()),
                new Parameter("text", dto.destinatario()),
                new Parameter("text", dto.coordenadas())
        ), null, null);
        var button = new Component("button", List.of(new Parameter("text", dto.coordenadas())), "url", "0");


        var template = new Template(
                props.getTemplateName(),
                new Language(props.getTemplateLanguage()),
                List.of(header, body, button)
        );

        return new WhatsAppMessage("whatsapp", dto.numero(), "template", template);
    }
}