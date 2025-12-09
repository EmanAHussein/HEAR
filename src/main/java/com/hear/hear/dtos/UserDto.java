package com.hear.hear.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    private final Integer id;
    private final String name;
    private final String email;
    private final String role;

//    public UserDto(Integer id, String name, String email, String role) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.role = role;
//    }
}
