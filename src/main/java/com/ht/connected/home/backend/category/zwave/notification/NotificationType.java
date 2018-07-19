package com.ht.connected.home.backend.category.zwave.notification;

import com.ht.connected.home.backend.category.zwave.constants.commandclass.BinarySwitchCommandClass;

public class NotificationType {
    
    public enum ZwaveNotification{
        not_notification
        , Smoke_Alarm
        , CO_Alarm
        , CO2_Alarm
        , Heat_Alarm
        , Water_Alarm 
        , Access_Control
        , Home_Security
        , Power_Management 
        , System 
        , Emergency_Alarm
        , Clock
        , Appliance
        , Home_Health
        , Siren
        , Water_Valve
        , Weather_Alarm 
        , Irrigation 
        , Gas_Alarm
    }
    enum AccessControl{
        Access_Control;
    }
    
    public static String getZwaveNotificationName(int notificationCode) {
        switch(notificationCode) {
            case(1):
                return ZwaveNotification.Smoke_Alarm.name();
            case(2):
                return ZwaveNotification.CO_Alarm.name();
            case(3):
                return ZwaveNotification.CO2_Alarm.name();
            case(4):
                return ZwaveNotification.Heat_Alarm.name();
            case(5):
                return ZwaveNotification.Water_Alarm.name();
            case(6):
                return ZwaveNotification.Access_Control.name();
            case(7):
                return ZwaveNotification.Home_Security.name();
            case(8):
                return ZwaveNotification.Power_Management.name();
            case(9):
                return ZwaveNotification.System.name();
            case(10):
                return ZwaveNotification.Emergency_Alarm.name();
            case(11):
                return ZwaveNotification.Clock.name();
            case(12):
                return ZwaveNotification.Appliance.name();
            case(13):
                return ZwaveNotification.Home_Health.name();
            case(14):
                return ZwaveNotification.Siren.name();
            case(15):
                return ZwaveNotification.Water_Valve.name();
            case(16):
                return ZwaveNotification.Weather_Alarm.name();
            case(17):
                return ZwaveNotification.Irrigation.name();
            case(18):
                return ZwaveNotification.Gas_Alarm.name();
            default:
                return ZwaveNotification.Water_Valve.name();
        }
    }
    
    
    public String getEventName(int notificationCode, int eventCode) {
        switch(notificationCode) {
        case(1):
            return ZwaveNotification.Smoke_Alarm.name();
        case(2):
            return ZwaveNotification.CO_Alarm.name();
        case(3):
            return ZwaveNotification.CO2_Alarm.name();
        case(4):
            return ZwaveNotification.Heat_Alarm.name();
        case(5):
            return ZwaveNotification.Water_Alarm.name();
        case(6):
            return getAccessControlEventName(eventCode);
        case(7):
            return ZwaveNotification.Home_Security.name();
        case(8):
            return ZwaveNotification.Power_Management.name();
        case(9):
            return ZwaveNotification.System.name();
        case(10):
            return ZwaveNotification.Emergency_Alarm.name();
        case(11):
            return ZwaveNotification.Clock.name();
        case(12):
            return ZwaveNotification.Appliance.name();
        case(13):
            return ZwaveNotification.Home_Health.name();
        case(14):
            return ZwaveNotification.Siren.name();
        case(15):
            return ZwaveNotification.Water_Valve.name();
        case(16):
            return ZwaveNotification.Weather_Alarm.name();
        case(17):
            return ZwaveNotification.Irrigation.name();
        case(18):
            return ZwaveNotification.Gas_Alarm.name();
        default:
            return ZwaveNotification.Water_Valve.name();
        }
    }
    
    public String getAccessControlEventName(int eventCode) {
        switch(eventCode) {
        case(1):
            return ZwaveNotification.Smoke_Alarm.name();
        case(2):
            return ZwaveNotification.CO_Alarm.name();
        case(3):
            return ZwaveNotification.CO2_Alarm.name();
        case(4):
            return ZwaveNotification.Heat_Alarm.name();
        case(5):
            return ZwaveNotification.Water_Alarm.name();
        case(6):
            return ZwaveNotification.Access_Control.name();
        case(7):
            return ZwaveNotification.Home_Security.name();
        case(8):
            return ZwaveNotification.Power_Management.name();
        case(9):
            return ZwaveNotification.System.name();
        case(10):
            return ZwaveNotification.Emergency_Alarm.name();
        case(11):
            return ZwaveNotification.Clock.name();
        case(12):
            return ZwaveNotification.Appliance.name();
        case(13):
            return ZwaveNotification.Home_Health.name();
        case(14):
            return ZwaveNotification.Siren.name();
        case(15):
            return ZwaveNotification.Water_Valve.name();
        case(16):
            return ZwaveNotification.Weather_Alarm.name();
        case(17):
            return ZwaveNotification.Irrigation.name();
        case(18):
            return ZwaveNotification.Gas_Alarm.name();
        default:
            return ZwaveNotification.Water_Valve.name();
        }
    }

}
