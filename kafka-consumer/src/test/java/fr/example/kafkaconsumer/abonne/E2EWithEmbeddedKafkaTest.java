package fr.example.kafkaconsumer.abonne;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import fr.example.kafkaconsumer.abonne.config.EmbeddedProducerConfig;
import fr.example.kafka.schema.abonne.AbonneEvent;
import fr.example.kafka.schema.abonne.AbonneMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@EmbeddedKafka(
        kraft = true,
        topics = "${abonne.abonne-topic}",
        partitions = 1
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(EmbeddedProducerConfig.class)
@ActiveProfiles("test")
@DisplayName("📩 Test de consommation avec EmbeddedKafka")
@ExtendWith(OutputCaptureExtension.class)
class E2EWithEmbeddedKafkaTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private KafkaTemplate<String, AbonneMessage> kafkaTemplate;

    @Value("${abonne.abonne-topic}")
    private String abonneTopic;

    @DisplayName("📋 Doit logguer la consommation d'un message")
    @Test
    void should_log_consumed_message(CapturedOutput output) {
        // Given ⌖
        final var uuid = UUID.randomUUID();
        final var creationDate = Instant.now();
        final var message = AbonneMessage.newBuilder()
                .setAbonneEvent(AbonneEvent.newBuilder()
                        .setId(uuid)
                        .setNom("Dupont")
                        .setPrenom("Marcel")
                        .setAdresse("20 rue Marcel Pagnol")
                        .setDateCreation(creationDate)
                        .build())
                .build();
        final var producerRecord = new ProducerRecord<>(abonneTopic, uuid.toString(), message);

        // When 👉
        kafkaTemplate.send(producerRecord).join();

        // Then ✅
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> assertThat(output).contains("Abonne: "));
    }
}
