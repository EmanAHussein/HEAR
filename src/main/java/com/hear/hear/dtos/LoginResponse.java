package com.hear.hear.dtos;

import com.hear.hear.authentication.Jwt;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Jwt accessToken;
    private Jwt refreshToken;
}
