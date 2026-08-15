package com.juanespinosa.atlas.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String message
) {}