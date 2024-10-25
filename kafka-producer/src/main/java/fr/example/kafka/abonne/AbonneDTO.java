package fr.example.kafka.abonne;

import java.time.LocalDateTime;
import java.util.UUID;

public record AbonneDTO(
    UUID id,
    String prenom,
    String nom,
    String adresse,
    String statut,
    LocalDateTime dateCreation
) {
}
