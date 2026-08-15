package com.juanespinosa.atlas.auth;

public record LoginRequest(
        String email,
        String password
) {}