package com.example.authapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    public record RegisterRequest(@Email @NotBlank String email,
                                  @Size(min=4) String password) {

    }

    public record LoginRequest(@Email @NotBlank String email,
                               @NotBlank String password) {

    }

    public record JwtResponse(String token) {}

    public record  ProcessRequest (@NotBlank String text){}

    public record TransformRequest(String text){}

    public record TransformResponse(String result){}
}
