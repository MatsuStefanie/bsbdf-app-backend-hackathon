package com.javali.hackathon.bsbdfappbackendhackathon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private String nome;
    private String telefone;
    private boolean ativo;
}