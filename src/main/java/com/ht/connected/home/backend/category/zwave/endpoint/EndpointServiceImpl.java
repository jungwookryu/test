package com.ht.connected.home.backend.category.zwave.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.category.zwave.notification.Notification;
import com.ht.connected.home.backend.category.zwave.notification.NotificationRepository;
import com.ht.connected.home.backend.category.zwave.notification.NotificationService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class EndpointServiceImpl extends CrudServiceImpl<Endpoint, Integer> implements EndpointService {

    private EndpointRepository endpointRepository;

    @Autowired
    CmdClsRepository cmdClsRepository;
    
    @Autowired
    NotificationRepository notificationRepository;
    
    @Autowired
    public EndpointServiceImpl(EndpointRepository endpointRepository) {
        super(endpointRepository);
        this.endpointRepository = endpointRepository;
    }

    @Autowired
    ZWaveRepository zWaveRepository;
    
    @Autowired
    NotificationService notificationService;
    
    private static final Log logging = LogFactory.getLog(EndpointServiceImpl.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @Override
    public ZWave modify(int no , Endpoint endpoint) {
        Endpoint saveEndpoint = endpointRepository.findOne(no);
        if(saveEndpoint != null) {
            if(0==saveEndpoint.getEpid()) {
                zWaveRepository.setModifyNicknameForNo(endpoint.getNickname(), saveEndpoint.getZwaveNo());
            }
            saveEndpoint.setNickname(endpoint.getNickname());
            endpointRepository.setModifyNicknameForNo(endpoint.getNickname(), no);
            return zWaveRepository.findOne(saveEndpoint.getZwaveNo());
        }else {
            return new ZWave();
        }
    }
    @Transactional
    @Override
    public void deleteEndpoint(ZWave zWave) {
        List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zWave.getNo());
        for (Endpoint endpoint : lstEndpoint) {
            cmdClsRepository.deleteByEndpointNo(endpoint.getNo());
            notificationService.delete(zWave);
        }
        endpointRepository.deleteByZwaveNo(zWave.getNo());
    }
    
    @Override
    public List<EndpointReportByApp> getEndpoint(ZWave zwave) {
        List<EndpointReportByApp> lstEndpointReportByApp = new ArrayList<>();
        List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zwave.getNo());
        for (int j = 0; j < lstEndpoint.size(); j++) {
            Endpoint endpoint = lstEndpoint.get(j);
            EndpointReportByApp endpointReportByApp = new EndpointReportByApp();
            endpointReportByApp.setEndpointNo(endpoint.getNo());
            endpointReportByApp.setEpStatus(endpoint.getStatus());
            endpointReportByApp.setEpid(endpoint.getEpid());
            endpointReportByApp.setNickname(endpoint.getNickname());
            endpointReportByApp.setFunctionCode(endpoint.getFunctionCode());
            endpointReportByApp.setDeviceTypeCode(endpoint.getGeneric()+"."+endpoint.getSpecific());
            endpointReportByApp.setDeviceTypeName(endpoint.getDeviceTypeName());
            endpointReportByApp.setNotifications(notificationService.getNotification(endpoint));
            
            lstEndpointReportByApp.add(endpointReportByApp);
        }
        return lstEndpointReportByApp;
    }
    
}
