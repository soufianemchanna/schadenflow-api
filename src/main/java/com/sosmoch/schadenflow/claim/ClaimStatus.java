package com.sosmoch.schadenflow.claim;

import java.util.Collection;
import java.util.List;

public enum ClaimStatus {
    SUBMITTED,
    UNDER_REVIEW,
    VALIDATED,
    REJECTED,
    ASSIGNED,
    IN_PROGRESS,
    NEED_MORE_INFO,
    RESOLVED,
    CLOSED;

    public static Collection<ClaimStatus> activeStatus(){
        return List.of(
                 UNDER_REVIEW, ASSIGNED, IN_PROGRESS, NEED_MORE_INFO
        );
    }
}
