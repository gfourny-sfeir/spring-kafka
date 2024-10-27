package fr.example.kafka.abonne.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import fr.example.kafka.schema.abonne.AbonneMessage;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

@TestConfiguration
public class EmbeddedProducerConfig {

    @Bean
    KafkaTemplate<String, AbonneMessage> kafkaTemplate(
            EmbeddedKafkaBroker broker,
            @Value("${spring.kafka.properties.schema.registry.url}") String schemaRegistryUrl) {

        var producerProps = KafkaTestUtils.producerProps(broker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        var pfs = new DefaultKafkaProducerFactory<String, AbonneMessage>(producerProps);
        return new KafkaTemplate<>(pfs);
    }
}
