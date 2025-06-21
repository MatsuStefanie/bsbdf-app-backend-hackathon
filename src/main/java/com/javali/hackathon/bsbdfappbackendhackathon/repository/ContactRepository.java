package com.javali.hackathon.bsbdfappbackendhackathon.repository;

import com.javali.hackathon.bsbdfappbackendhackathon.model.Contact;
import com.javali.hackathon.bsbdfappbackendhackathon.model.UserContacts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ContactRepository {

    private List<UserContacts> userContactsList = new ArrayList<>();

    public void setInitialData(List<UserContacts> initialData) {
        if (initialData != null && !initialData.isEmpty()) {
            this.userContactsList = new ArrayList<>(initialData);
            log.info("Repositório de contatos inicializado com sucesso com {} listas de usuários.", this.userContactsList.size());
        } else {
            log.warn("Nenhum dado inicial fornecido para o repositório de contatos.");
        }
    }


    public Optional<UserContacts> findByPessoaBase(String pessoaBase) {
        return userContactsList.stream()
                .filter(user -> user.getPessoaBase().equalsIgnoreCase(pessoaBase))
                .findFirst();
    }

    public List<Contact> findAllActiveContacts() {
        if (userContactsList.isEmpty()) {
            return Collections.emptyList();
        }
        return userContactsList.stream()
                .flatMap(user -> user.getContatos().stream())
                .filter(Contact::isAtivo)
                .collect(Collectors.toList());
    }

    public Optional<Contact> findByTelephone(String telephone) {
        return userContactsList.stream()
                .flatMap(user -> user.getContatos().stream())
                .filter(contact -> contact.getTelefone().equals(telephone))
                .findFirst();
    }

    public void add(UserContacts userContacts) {
        this.userContactsList.add(userContacts);
        log.info("Novo usuário '{}' adicionado à lista em memória.", userContacts.getPessoaBase());
    }
}