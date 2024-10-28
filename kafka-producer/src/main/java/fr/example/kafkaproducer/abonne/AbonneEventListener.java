package fr.example.kafkaproducer.abonne;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import fr.example.kafka.schema.abonne.AbonneMessage;
import fr.example.kafkaproducer.abonne.config.AbonneProperties;
import fr.example.kafkaproducer.abonne.mapper.AbonneMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
class AbonneEventListener {

    private final AbonneMapper abonneMapper;
    private final KafkaTemplate<String, AbonneMessage> kafkaTemplate;
    private final AbonneProperties abonneProperties;

    @EventListener
    void publish(AbonneDTO abonneDTO) {

        final var abonneMessage = AbonneMessage.newBuilder()
                .setAbonneEvent(abonneMapper.toAbonneEvent(abonneDTO))
                .build();

        final var producerRecord = new ProducerRecord<>(
                abonneProperties.abonneTopic(),
                abonneDTO.id().toString(),
                abonneMessage
        );

        kafkaTemplate.send(producerRecord)
                .whenComplete((result, throwable) -> {
                    if (nonNull(throwable)) {
                        log.error("Une erreur est survenue durant l'émission de l'abonné {}, \n Exception: {}", abonneDTO, throwable.getMessage());
                    } else {
                        log.info("""
                                L'abonné {} a bien été envoyé
                                avec la clef {}
                                sur le topic {}
                                """, abonneMessage, result.getProducerRecord().key(), result.getProducerRecord().topic());
                    }
                });
    }
}
