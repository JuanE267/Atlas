package com.juanespinosa.atlas.auth.user;

public record UserResponse(
        Long id,
        String email,
        Role role
) {}