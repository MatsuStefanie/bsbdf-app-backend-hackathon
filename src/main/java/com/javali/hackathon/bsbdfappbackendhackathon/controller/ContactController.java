package com.javali.hackathon.bsbdfappbackendhackathon.controller;

import com.javali.hackathon.bsbdfappbackendhackathon.model.Contact;
import com.javali.hackathon.bsbdfappbackendhackathon.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/{pessoaBase}/contacts")
    public ResponseEntity<List<Contact>> listContacts(
            @PathVariable String pessoaBase) {

        List<Contact> contacts;


        contacts = contactService.getContactsForUser(pessoaBase);


        return ResponseEntity.ok(contacts);
    }


    @PostMapping("/{pessoaBase}/contacts")
    public ResponseEntity<Contact> addContact(
            @PathVariable String pessoaBase,
            @RequestBody @Valid Contact contact) {

        Contact createdContact = contactService.addContact(pessoaBase, contact);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{telephone}")
                .buildAndExpand(createdContact.getTelefone())
                .toUri();

        return ResponseEntity.created(location).body(createdContact);
    }

    @PutMapping("/{pessoaBase}/contacts/{telephone}/desactive")
    public ResponseEntity<Void> deactivateContact(@PathVariable String pessoaBase, @PathVariable String telephone) {
        contactService.desactivateContact(pessoaBase, telephone);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{pessoaBase}/contacts/{telephone}/active")
    public ResponseEntity<Void> activateContact(@PathVariable String pessoaBase, @PathVariable String telephone) {
        contactService.activateContact(pessoaBase, telephone);
        return ResponseEntity.noContent().build();
    }
}