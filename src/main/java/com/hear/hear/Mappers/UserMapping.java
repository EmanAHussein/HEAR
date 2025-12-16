package com.hear.hear.Mappers;

import com.hear.hear.dtos.RegisterRequest;
import com.hear.hear.dtos.UserDto;
import com.hear.hear.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapping {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);

}
