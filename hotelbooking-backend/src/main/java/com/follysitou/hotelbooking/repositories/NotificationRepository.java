package com.follysitou.hotelbooking.repositories;

import com.follysitou.hotelbooking.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
