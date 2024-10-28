package fr.example.kafkaproducer.abonne.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import fr.example.kafka.schema.abonne.AbonneMessage;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

@TestConfiguration
public class EmbeddedConsumerConfig {

    @Bean
    public Consumer<String, AbonneMessage> consumer(
            EmbeddedKafkaBroker broker,
            @Value("${spring.kafka.properties.schema.registry.url}") String schemaRegistryUrl) {

        var consumerProps = KafkaTestUtils.consumerProps("abonnee-consumer", "true", broker);
        consumerProps.replace(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.replace(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_VALUE_TYPE_CONFIG, AbonneMessage.class);
        consumerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

        var cf = new DefaultKafkaConsumerFactory<String, AbonneMessage>(consumerProps);
        return cf.createConsumer();
    }
}
