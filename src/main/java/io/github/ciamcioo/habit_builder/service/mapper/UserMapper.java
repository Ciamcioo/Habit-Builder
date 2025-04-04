package io.github.ciamcioo.habit_builder.service.mapper;

import io.github.ciamcioo.habit_builder.exception.ConversionException;
import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unexpectedValueMappingException = ConversionException.class)
public interface UserMapper {

    @Mapping(target = "email" ,    source = "entity.email")
    @Mapping(target = "username",  source = "entity.username")
    @Mapping(target = "firstName", source = "entity.firstName")
    @Mapping(target = "lastName",  source = "entity.lastName")
    @Mapping(target = "age",       source = "entity.age")
    UserDTO toDTO(User entity);

    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "email",      source = "dto.email")
    @Mapping(target = "username",   source = "dto.username")
    @Mapping(target = "firstName",  source = "dto.firstName")
    @Mapping(target = "lastName",   source = "dto.lastName")
    @Mapping(target = "age",        source = "dto.age")
    @Mapping(target = "userHabits", ignore = true)
    User toEntity(UserDTO dto);
}
