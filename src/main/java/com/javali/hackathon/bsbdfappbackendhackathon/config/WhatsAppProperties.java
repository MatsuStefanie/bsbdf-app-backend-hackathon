package com.javali.hackathon.bsbdfappbackendhackathon.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "hacka.meta.whatsapp.api")
public class WhatsAppProperties {
    private String token;
    private String phoneNumberId;
    private String url;
    private String templateName;
    private String templateLanguage;
}