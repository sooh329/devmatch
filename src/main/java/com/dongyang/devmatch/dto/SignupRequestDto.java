package com.dongyang.devmatch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequestDto {
    private String email;
    private String username;
    private String password;
    private String passwordConfirm;
}