package com.sosmoch.schadenflow.core.job;

import java.util.UUID;

public record AsyncJobDto(
        UUID id,
        AsyncJobStatus status
) {
}
