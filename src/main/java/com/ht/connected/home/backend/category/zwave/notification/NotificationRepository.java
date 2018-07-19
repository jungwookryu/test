package com.ht.connected.home.backend.category.zwave.notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    
    Notification findByNotificationCodeAndEndpointNo(int notificationCode, int endpointNo);
    void deleteByZwaveNo(int zwaveNo);
}
