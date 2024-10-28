package fr.example.kafkaproducer.abonne;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record AbonneDTO(
        @Schema(name = "id", description = "Id de l'abonné")
        UUID id,
        @Schema(name = "prenom", description = "Prenom de l'abonné")
        String prenom,
        @Schema(name = "nom", description = "Nom de l'abonné")
        String nom,
        @Schema(name = "adresse", description = "Adresse de l'abonné")
        String adresse,
        @Schema(description = "Statut de l'abonné : INACTIF, ACTIF, SUSPENDU, RESILIE", allowableValues = { "INACTIF", "ACTIF", "SUSPENDU", "RESILIE" })
        String statut,
        @Schema(name = "dateCreation", description = "Date de création de l'abonné")
        LocalDateTime dateCreation
) {
}
