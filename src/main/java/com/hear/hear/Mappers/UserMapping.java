package com.hear.hear.Mappers;

import com.hear.hear.dtos.RegisterUserRequest;
import com.hear.hear.dtos.UpdateUserDto;
import com.hear.hear.dtos.UserDto;
import com.hear.hear.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapping {
    User toUser(RegisterUserRequest registerUserRequest);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(
            UpdateUserDto updateUserDto,
            @MappingTarget User user
    );

    UserDto toDto(User user);

}