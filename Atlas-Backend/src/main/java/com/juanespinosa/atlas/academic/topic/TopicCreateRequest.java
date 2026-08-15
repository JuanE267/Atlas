package com.juanespinosa.atlas.academic.topic;

public record TopicCreateRequest(
        String title,
        String content,
        Long subjectId
) {}