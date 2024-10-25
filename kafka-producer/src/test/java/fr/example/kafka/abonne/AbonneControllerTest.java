package fr.example.kafka.abonne;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("🎯 Le controller expose la route /api/abonne")
@WebMvcTest(AbonneController.class)
@RecordApplicationEvents
class AbonneControllerTest {

    @Autowired
    ApplicationEvents events;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("📤 et doit traiter la demande de publication d'un abonne")
    @Test
    void should_treat_publishing_abonne() throws Exception {
        // Given ⌖
        final var uuid = UUID.randomUUID();

        // When 👉
        final var body = """
                {
                  "id": "%s",
                  "nom": "Dupont",
                  "prenom": "Marc",
                  "adresse": "25 route plus loin 48770",
                  "statut": "INACTIF"
                }
                """.formatted(uuid);

        mockMvc.perform(post("/api/abonne")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpectAll(
                        status().isAccepted(),
                        content().json(body)
                );

        // Then ✅

        var abonneDTOS = events.stream(AbonneDTO.class).toList();

        assertThat(abonneDTOS).isNotEmpty()
                .hasSize(1)
                .first()
                .extracting(AbonneDTO::id)
                .isEqualTo(uuid);
    }
}