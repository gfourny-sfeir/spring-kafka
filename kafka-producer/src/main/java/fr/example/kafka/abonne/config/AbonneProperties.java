package fr.example.kafka.abonne.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "abonne")
public record AbonneProperties (
        @NotBlank String abonneTopic
) {
}
