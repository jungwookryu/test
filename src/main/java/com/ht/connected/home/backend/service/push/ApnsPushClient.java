package com.ht.connected.home.backend.service.push;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javapns.Push;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

@Service
public class ApnsPushClient {

    private static final Log LOGGER = LogFactory.getLog(ApnsPushClient.class);
    
    private String certificatePath = "src/main/resources/voip_cert_conv_media.p12";
    
    private String certificatePassword = "guseoxhdtls123";

    public List<PushedNotification> send(List<String> tokens, PushNotificationPayload payload) {
        List<PushedNotification> notifications = null;
        try {
            notifications = Push.payload(payload, certificatePath, certificatePassword, true, tokens);
            for (PushedNotification notification : notifications) {
                if (notification.isSuccessful()) {
                    LOGGER.info("APNs PUSH NOTIFICATION SENT SUCCESSFULLY TO: " + notification.getDevice().getToken());
                } else {
                    notification.getException().printStackTrace();
                    ResponsePacket responsePacket = notification.getResponse();
                    if (responsePacket != null) {
                        LOGGER.info(String.format("======== APNs error message for token : %s ========",
                                notification.getDevice().getToken()));
                        LOGGER.info(responsePacket.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifications;
    }

}
