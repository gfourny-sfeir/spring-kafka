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

    @Mapping(source = "dateCreation", target = "dateCreation", qualifiedByName = "mapToLocalDateTime")
    AbonneDTO toAbonneDTO(AbonneEvent abonneEvent);

    @Named("mapToLocalDateTime")
    default LocalDateTime mapToLocalDateTime(Instant instant) {
        return isNull(instant) ? LocalDateTime.now() : LocalDateTime.from(instant);
    }
}
