package com.backend.Domain.Login.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String username;
    private String email;
    private String Password;
    private String phoneNumber;
}
