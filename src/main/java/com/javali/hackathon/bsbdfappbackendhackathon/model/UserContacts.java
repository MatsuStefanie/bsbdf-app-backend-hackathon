package com.javali.hackathon.bsbdfappbackendhackathon.model;


import java.util.List;
import lombok.Data;

@Data
public class UserContacts {
    private String pessoaBase;
    private List<Contact> contatos;
}