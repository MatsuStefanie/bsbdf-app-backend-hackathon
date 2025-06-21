package com.javali.hackathon.bsbdfappbackendhackathon.service;

import com.javali.hackathon.bsbdfappbackendhackathon.DTO.AlertaRequest;
import com.javali.hackathon.bsbdfappbackendhackathon.model.Contact;
import com.javali.hackathon.bsbdfappbackendhackathon.model.LocationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {
    private final ContactService contactService;
    private final WhatsAppAlertaService whatsAppAlertaService;


    public void processLocation(String pessoaBase, LocationRequest location) {
        log.info("Localização recebida de '{}': Latitude={}, Longitude={}",
                pessoaBase,
                location.getLatitude(),
                location.getLongitude()
        );

        try {
            List<Contact> activeContacts = contactService.getActiveContactsForUser(pessoaBase);

            if (activeContacts.isEmpty()) {
                log.warn("Nenhum contato ativo foi encontrado para o usuário '{}'. Nenhuma notificação será enviada.", pessoaBase);
                return;
            }

            log.info("Encontrados {} contatos ativos para '{}'. Iniciando processo de notificação via WhatsApp.", activeContacts.size(), pessoaBase);

            String mapsLink = String.format("https://www.google.com/maps?q=%s,%s",
                    location.getLatitude(),
                    location.getLongitude()
            );
            String coordenadas = String.format("%s,%S", location.getLatitude(),
                    location.getLongitude());

            for (Contact contact : activeContacts) {
                try {
                    AlertaRequest alerta = new AlertaRequest(
                            contact.getTelefone(),
                            pessoaBase,
                            contact.getNome(),
                            mapsLink,
                            coordenadas

                    );

                    whatsAppAlertaService.enviarAlerta(alerta);

                } catch (Exception e) {
                    log.error("Falha ao tentar notificar o contato '{}' (de {}) no número {}: {}",
                            contact.getNome(), pessoaBase, contact.getTelefone(), e.getMessage());
                }
            }

            log.info("Processo de notificação para '{}' concluído.", pessoaBase);

        } catch (NoSuchElementException e) {
            log.error("Tentativa de processar localização para um usuário que não existe: '{}'", pessoaBase);
        }
    }
}