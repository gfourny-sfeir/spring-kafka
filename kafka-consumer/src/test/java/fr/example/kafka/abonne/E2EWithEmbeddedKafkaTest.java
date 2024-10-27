package fr.example.kafka.abonne;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import fr.example.kafka.abonne.config.EmbeddedProducerConfig;
import fr.example.kafka.schema.abonne.AbonneMessage;

@EmbeddedKafka(
        topics = "${abonne.abonne-topic}",
        partitions = 1
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(EmbeddedProducerConfig.class)
@ActiveProfiles("test")
@DisplayName("Test de consommation avec EmbeddedKafka")
class E2EWithEmbeddedKafkaTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private KafkaTemplate<String, AbonneMessage> kafkaTemplate;
}
