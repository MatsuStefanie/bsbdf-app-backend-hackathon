package com.javali.hackathon.bsbdfappbackendhackathon.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;


public record WhatsAppMessage(
        @JsonProperty("messaging_product") String messagingProduct,
        String to,
        String type,
        Template template
) {}