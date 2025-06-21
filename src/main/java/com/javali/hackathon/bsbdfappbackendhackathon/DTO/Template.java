package com.javali.hackathon.bsbdfappbackendhackathon.DTO;

import java.util.List;

public record Template(
        String name,
        Language language,
        List<Component> components
) {}