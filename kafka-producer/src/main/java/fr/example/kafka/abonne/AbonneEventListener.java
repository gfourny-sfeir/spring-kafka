package fr.example.kafka.abonne;

import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import fr.example.kafka.abonne.mapper.AbonneMapper;
import fr.example.kafka.schema.abonne.AbonneMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
class AbonneEventListener {

    private final AbonneMapper abonneMapper;
    private final KafkaTemplate<String, AbonneMessage> kafkaTemplate;

    @EventListener
    void publish(AbonneDTO abonneDTO) {

        final var abonneMessage = AbonneMessage.newBuilder()
                .setAbonneEvent(abonneMapper.toAbonneEvent(abonneDTO))
                .build();

        kafkaTemplate.send(abonneDTO.id().toString(), abonneMessage)
                .whenComplete((result, throwable) -> {
                    if (nonNull(throwable)) {
                        log.error("Une erreur est survenue durant l'émission de l'abonné {}, \n Exception: {}", abonneDTO, throwable.getMessage());
                    } else {
                        log.info("""
                                L'abonné {} a bien été envoyé
                                avec la clef {}
                                sur le topic {} partition {}
                                """, abonneDTO, result.getProducerRecord().key(), result.getProducerRecord().topic(), result.getProducerRecord().partition());
                    }
                });
    }
}
