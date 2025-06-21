package com.javali.hackathon.bsbdfappbackendhackathon.service;

import com.javali.hackathon.bsbdfappbackendhackathon.model.Contact;
import com.javali.hackathon.bsbdfappbackendhackathon.model.UserContacts;
import com.javali.hackathon.bsbdfappbackendhackathon.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository repository;

    public List<Contact> getContactsForUser(String pessoaBase) {
        log.info("Buscando todos os contatos para o usuário: {}", pessoaBase);

        return repository.findByPessoaBase(pessoaBase)
                .map(UserContacts::getContatos)
                .orElse(Collections.emptyList());
    }
    public List<Contact> getActiveContactsForUser(String pessoaBase) {
        log.info("Buscando contatos ativos para o usuário: {}", pessoaBase);

        UserContacts userContacts = repository.findByPessoaBase(pessoaBase)
                .orElseThrow(() -> new NoSuchElementException("Usuário '" + pessoaBase + "' não encontrado."));

       return userContacts.getContatos().stream()
                .filter(Contact::isAtivo)
                .toList();
    }

    public Contact addContact(String pessoaBase, Contact newContact) {

        UserContacts userContacts = repository.findByPessoaBase(pessoaBase)
                .orElseThrow(() -> new NoSuchElementException("Usuário '" + pessoaBase + "' não encontrado. Não é possível adicionar o contato."));

        boolean phoneExists = userContacts.getContatos().stream()
                .anyMatch(existingContact -> existingContact.getTelefone().equals(newContact.getTelefone()));

        if (phoneExists) {
            throw new IllegalArgumentException("Um contato com o telefone '" + newContact.getTelefone() + "' já existe para o usuário '" + pessoaBase + "'.");
        }

        userContacts.getContatos().add(newContact);
        log.info("Novo contato '{}' adicionado com sucesso para o usuário '{}'.", newContact.getNome(), pessoaBase);

        return newContact;
    }

    public void desactivateContact(String pessoaBase, String telephone) {
        Contact contact = findContactForUser(pessoaBase, telephone);
        contact.setAtivo(false);
        log.info("Contato com telefone '{}' do usuário base '{}' foi desativado com sucesso.", telephone, pessoaBase);
    }


    public void activateContact(String pessoaBase, String telephone) {
        Contact contact = findContactForUser(pessoaBase, telephone);
        contact.setAtivo(true);
        log.info("Contato com telefone '{}' do usuário base '{}' foi ativado com sucesso.", telephone, pessoaBase);
    }


    private Contact findContactForUser(String pessoaBase, String telephone) {
        UserContacts userContacts = repository.findByPessoaBase(pessoaBase)
                .orElseThrow(() -> new NoSuchElementException("Usuário '" + pessoaBase + "' não encontrado."));

        return userContacts.getContatos().stream()
                .filter(c -> c.getTelefone().equals(telephone))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Contato com o telefone '" + telephone + "' não encontrado para o usuário base '" + pessoaBase + "'."));
    }
}