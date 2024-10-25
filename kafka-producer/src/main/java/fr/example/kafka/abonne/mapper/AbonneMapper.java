package fr.example.kafka.abonne.mapper;

import java.time.Instant;
import java.time.LocalDateTime;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import fr.example.kafka.abonne.AbonneDTO;
import fr.example.kafka.schema.abonne.AbonneEvent;

import static java.util.Objects.isNull;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = SPRING)
public interface AbonneMapper {

    @Mapping(source = "dateCreation", target = "dateCreation", qualifiedByName = "mapToInstant")
    AbonneEvent toAbonneEvent(AbonneDTO abonneDTO);

    @Named("mapToInstant")
    default Instant mapToInstant(LocalDateTime localDateTime) {
        return isNull(localDateTime) ? Instant.now() : Instant.from(localDateTime);
    }
}
