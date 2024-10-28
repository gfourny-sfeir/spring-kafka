package fr.example.kafkaproducer.abonne.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import fr.example.kafka.schema.abonne.AbonneEvent;
import fr.example.kafkaproducer.abonne.AbonneDTO;

import static java.util.Objects.isNull;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = SPRING)
public interface AbonneMapper {

    @Mapping(source = "dateCreation", target = "dateCreation", qualifiedByName = "mapToInstant")
    AbonneEvent toAbonneEvent(AbonneDTO abonneDTO);

    @Named("mapToInstant")
    default Instant mapToInstant(LocalDateTime localDateTime) {
        return isNull(localDateTime) ? Instant.now() : localDateTime.toInstant(ZoneOffset.UTC);
    }
}
