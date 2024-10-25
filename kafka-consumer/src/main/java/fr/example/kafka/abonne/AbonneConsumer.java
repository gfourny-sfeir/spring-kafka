package fr.example.kafka.abonne;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import fr.example.kafka.abonne.mapper.AbonneMapper;
import fr.example.kafka.schema.abonne.AbonneMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AbonneConsumer {

    private final AbonneMapper abonneMapper;

    @KafkaListener
    public void consumeAbonne(AbonneMessage abonneMessage) {
        final var abonneDTO = abonneMapper.toAbonneDTO(abonneMessage.getAbonneEvent());
    }
}
