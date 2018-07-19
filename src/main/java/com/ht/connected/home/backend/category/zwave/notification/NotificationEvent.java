package com.ht.connected.home.backend.category.zwave.notification;

import com.ht.connected.home.backend.category.zwave.notification.NotificationType.ZwaveNotification;

public class NotificationEvent {
    
    private static String Open ="Open"; 
    private static String Close ="Close"; 
    private static String On ="On"; 
    private static String Off ="Off";
    public static final int intOn =3;
    public static final int intOff =2;
    private static String NoAccessCotrol ="NoAccessCotrol"; 
    private static String Fire_Detector ="fire detector"; 
    private static String Medical_Detector ="medical detector"; 
    private static String Motion_Detection ="Motion Detection "; 
    private static String Home_Security_detection ="Home Security detection"; 
    private static String Motion_Detection_Unknown_Location ="Motion Detection, Unknown Location";
    private static String No_Motion_Detection="No Motion Detection";
    
    
    
    public static String getEventName(int notificationCode, int eventCode) {
        
        if(notificationCode == ZwaveNotification.Access_Control.ordinal()) {
            return accessControlEventName(eventCode);
        }else if(notificationCode == ZwaveNotification.Home_Security.ordinal()) {
            return homeSecurityEventName(eventCode);
        }else if(notificationCode == ZwaveNotification.Emergency_Alarm.ordinal()) {
            return emergencyAlarmEventName(eventCode);
        }else if(notificationCode == ZwaveNotification.Power_Management.ordinal()) {
            return powerManagementEventName(eventCode);
        }else
            return null;
        }
    
    private static String accessControlEventName(int eventCode) {
        if(eventCode == 22) {
            return Open;
        }else if(eventCode == 23) {
            return Close;
        }else {
            return NoAccessCotrol;
        }
    }
    
    private static String homeSecurityEventName(int eventCode) {
        if(eventCode == 0) {
            return Home_Security_detection;
        }else if(eventCode == 7) {
            return Motion_Detection;
        }else if(eventCode == 8) {
            return Motion_Detection_Unknown_Location;
        }else {
            return No_Motion_Detection;
        }
    }
    
    private static String emergencyAlarmEventName(int eventCode) {
        if(eventCode == 2) {
            return Fire_Detector;
        }else if(eventCode == 3) {
            return Medical_Detector;
        }else{
            return "No Emergency AlarmEvent";
        }
    }
    private static String powerManagementEventName(int eventCode) {
        if(eventCode == intOn) {
            return On;
        }else if(eventCode == intOff) {
            return Off;
        }else{
            return "No Power Management";
        }
    }
    
   
}
