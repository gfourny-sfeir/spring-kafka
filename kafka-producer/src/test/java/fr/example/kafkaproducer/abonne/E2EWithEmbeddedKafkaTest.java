package fr.example.kafkaproducer.abonne;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import fr.example.kafka.schema.abonne.AbonneEvent;
import fr.example.kafka.schema.abonne.AbonneMessage;
import fr.example.kafkaproducer.abonne.config.EmbeddedConsumerConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@EmbeddedKafka(
        kraft = true,
        topics = "${abonne.abonne-topic}",
        partitions = 1
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(EmbeddedConsumerConfig.class)
@ActiveProfiles("test")
@DisplayName("👩🏻 Test end to end avec EmbeddedKafka")
class E2EWithEmbeddedKafkaTest {

    @Autowired
    EmbeddedKafkaBroker broker;
    @Value("${abonne.abonne-topic}")
    String topic;
    @Autowired
    Consumer<String, AbonneMessage> consumer;
    RestClient restClient;
    @LocalServerPort
    private int port;

    @BeforeAll
    void init() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
        broker.consumeFromAnEmbeddedTopic(consumer, topic);
    }

    @AfterAll
    void tearDown() {
        this.consumer.close();
    }

    @DisplayName("📤 Doit publier un évènement lorsque l'API est appelée")
    @Test
    void should_publish_event() {
        // Given ⌖
        final var id = UUID.randomUUID();
        final var dateCreation = LocalDateTime.now();
        final var abonneDto = new AbonneDTO(
                id,
                "Marcel",
                "Dupont",
                "25 route plus loin 48770",
                "INACTIF",
                dateCreation
        );

        // When 👉
        final var entity = restClient.post()
                .uri("/api/abonne")
                .body(abonneDto)
                .retrieve()
                .toEntity(AbonneDTO.class);

        // Then ✅
        ConsumerRecords<String, AbonneMessage> records = KafkaTestUtils.getRecords(consumer);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entity)
                    .extracting(ResponseEntity::getStatusCode)
                    .isEqualTo(HttpStatus.ACCEPTED);

            softly.assertThat(entity)
                    .extracting(ResponseEntity::getBody)
                    .isNotNull()
                    .isEqualTo(abonneDto);
        });

        await().pollDelay(20, TimeUnit.MILLISECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(records)
                        .isNotEmpty()
                        .last()
                        .extracting(ConsumerRecord::value)
                        .isNotNull()
                        .extracting(AbonneMessage::getAbonneEvent)
                        .satisfies(abonneEvent -> assertThat(abonneEvent)
                                .isNotNull()
                                .extracting(AbonneEvent::getId, AbonneEvent::getPrenom, AbonneEvent::getNom, AbonneEvent::getAdresse)
                                .containsExactly(abonneDto.id(), abonneDto.prenom(), abonneDto.nom(), abonneDto.adresse())
                        )
                );
    }
}
