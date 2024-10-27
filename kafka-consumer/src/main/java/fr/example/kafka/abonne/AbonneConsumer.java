package fr.example.kafka.abonne;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import fr.example.kafka.abonne.mapper.AbonneMapper;
import fr.example.kafka.schema.abonne.AbonneMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AbonneConsumer {

    private final AbonneMapper abonneMapper;

    @KafkaListener(topics = "${abonne.abonne-topic}")
    public void consumeAbonne(ConsumerRecords<String, AbonneMessage> consumerRecords) {
        for (ConsumerRecord<String, AbonneMessage> record : consumerRecords) {
            log.info("Consommation du message clef: {}", record.key());
            record.headers().forEach(header -> log.info("Header key: {}, Header value: {}", header.key(), header.value()));
            final var abonneDTO = abonneMapper.toAbonneDTO(record.value().getAbonneEvent());
            log.info("Abonne: {}", abonneDTO);
        }
    }
}
