package com.elhaffar.exoformbackend.mapper;

import com.elhaffar.exoformbackend.dto.UserDto;
import com.elhaffar.exoformbackend.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
