package com.sosmoch.schadenflow.core.job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AsyncJobRepository extends JpaRepository<AsyncJob, UUID> {
}
