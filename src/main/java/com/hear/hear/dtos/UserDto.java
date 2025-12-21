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
    private final String admin;
//    private final boolean isAdmin;

}
