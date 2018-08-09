package com.ht.connected.home.backend.device.category.zwave.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    
    Notification findByNotificationCodeAndEndpointNo(int notificationCode, int endpointNo);
    
    List<Notification> findByEndpointNo(int endpointNo);
    
    void deleteByZwaveNo(int zwaveNo);
    
    void deleteByZwaveNoIn(List<Integer> zwaveNos);
}
