package io.github.ciamcioo.habit_builder.service.mapper;

import io.github.ciamcioo.habit_builder.exception.MappingException;
import io.github.ciamcioo.habit_builder.model.dto.HabitDTO;
import io.github.ciamcioo.habit_builder.model.entity.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  unexpectedValueMappingException = MappingException.class)
public interface HabitMapper {

    @Mapping(target = "name",      source = "entity.name")
    @Mapping(target = "frequency", source = "entity.frequency")
    @Mapping(target = "startDate", source = "entity.startDate")
    @Mapping(target = "endDate",   source = "entity.endDate")
    @Mapping(target = "reminder",  source = "entity.reminder")
    HabitDTO toDTO(Habit entity);


    @Mapping(target = "uuid",      ignore = true)
    @Mapping(target = "name",      source = "dto.name")
    @Mapping(target = "frequency", source = "dto.frequency")
    @Mapping(target = "startDate", source = "dto.startDate")
    @Mapping(target = "endDate",   source = "dto.endDate")
    @Mapping(target = "reminder",  source = "dto.reminder")
    @Mapping(target = "user",      ignore = true)
    Habit toEntity(HabitDTO dto);
}
