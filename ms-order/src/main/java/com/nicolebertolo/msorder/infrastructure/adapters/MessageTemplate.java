package com.nicolebertolo.msorder.infrastructure.adapters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplate<T> {
    private String origin;
    private String tracing;
    private String timestamp;
    private T message;
}
