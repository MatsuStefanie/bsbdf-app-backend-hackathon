package com.javali.hackathon.bsbdfappbackendhackathon.controller;

import com.javali.hackathon.bsbdfappbackendhackathon.model.LocationRequest;
import com.javali.hackathon.bsbdfappbackendhackathon.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/{pessoaBase}")
    public ResponseEntity<String> receiveLocation(
            @PathVariable String pessoaBase,
            @RequestBody @Valid LocationRequest location) {

        locationService.processLocation(pessoaBase, location);

        return ResponseEntity.ok("Localização de '" + pessoaBase + "' recebida com sucesso.");
    }
}