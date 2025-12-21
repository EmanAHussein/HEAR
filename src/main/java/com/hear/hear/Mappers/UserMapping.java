package com.hear.hear.Mappers;

import com.hear.hear.dtos.RegisterUserRequest;
import com.hear.hear.dtos.UpdateUserDto;
import com.hear.hear.dtos.UserDto;
import com.hear.hear.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapping {
    User toUser(RegisterUserRequest registerUserRequest);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(
            UpdateUserDto updateUserDto,
            @MappingTarget User user
    );

//    @Mapping(source = "admin", target = "isAdmin")
    UserDto toDto(User user);

}