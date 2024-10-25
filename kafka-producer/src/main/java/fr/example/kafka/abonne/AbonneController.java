package fr.example.kafka.abonne;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/abonne")
@RequiredArgsConstructor
class AbonneController {

    private final ApplicationEventPublisher eventPublisher;

    @PostMapping
    ResponseEntity<AbonneDTO> publish(@RequestBody AbonneDTO abonneDTO) {
        eventPublisher.publishEvent(abonneDTO);
        return ResponseEntity.accepted().body(abonneDTO);
    }
}
