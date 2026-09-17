package com.sosmoch.schadenflow.core.job;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "async_jobs")
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class AsyncJob {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AsyncJobStatus status;

    private int successCount;
    private int totalCount;

    @Column(columnDefinition = "TEXT")
    private String errorsJson;

    private Instant startedAt;
    private Instant finishedAt;
}
