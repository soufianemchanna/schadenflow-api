package com.sosmoch.schadenflow.notification.internal;

import com.sosmoch.schadenflow.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId);
    long countByRecipientIdAndReadIsFalse(UUID recipientId);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.recipientId = :recipientId")
    void markAllAsReadForRecipient(UUID recipientId);
}
