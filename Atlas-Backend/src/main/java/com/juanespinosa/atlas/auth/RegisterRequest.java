package com.juanespinosa.atlas.auth;

public record RegisterRequest(
        String email,
        String password
) {}