package com.hear.hear.Mappers;

import com.hear.hear.dtos.RegisterRequest;
import com.hear.hear.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterUserRequest {
    @Mapping(source = "password",target = "hashedPassword")
    User toRegister(RegisterRequest registerRequest);


}
