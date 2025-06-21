package com.javali.hackathon.bsbdfappbackendhackathon.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlertaRequest(
        String numero,
        String nomeAlerta,
        String destinatario,
        String linkMap,
        String coordenadas) {
}
