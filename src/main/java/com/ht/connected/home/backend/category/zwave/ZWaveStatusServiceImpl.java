package com.ht.connected.home.backend.category.zwave;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.category.zwave.notification.RequestNotification;
import com.ht.connected.home.backend.category.zwave.notification.Notification;
@Service
public class ZWaveStatusServiceImpl implements ZWaveStatusService {
    
    enum ZwaveNotification{
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
    
    ObjectMapper ObjectMapper = new ObjectMapper();
    
    @Autowired
    EndpointRepository endpointRepository;
    
    @Override
    public void subscribe(ZWaveRequest zwaveRequest, String payload) {
        
        int endpointId = zwaveRequest.getEndpointId();
        Endpoint endpoint = endpointRepository.findOne(endpointId);
        RequestNotification requestNotification = ObjectMapper.convertValue(payload, RequestNotification.class);
        String deviceTypeCode =endpoint.getGeneric()+endpoint.getSpecific();
        Notification notification = new Notification(requestNotification.getNotificationType(), requestNotification.getMevent()
                , requestNotification.getSequence(), deviceTypeCode);
        
    }
}
