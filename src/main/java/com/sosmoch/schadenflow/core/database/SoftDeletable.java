package com.sosmoch.schadenflow.core.database;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class SoftDeletable {
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isDeleted(){
        return deletedAt != null;
    }
}
