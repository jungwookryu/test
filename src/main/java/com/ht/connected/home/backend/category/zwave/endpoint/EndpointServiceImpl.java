package com.ht.connected.home.backend.category.zwave.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class EndpointServiceImpl extends CrudServiceImpl<Endpoint, Integer> implements EndpointService {

    private EndpointRepository endpointRepository;
    
    @Autowired
    public EndpointServiceImpl(EndpointRepository endpointRepository) {
        super(endpointRepository);
        this.endpointRepository = endpointRepository;
    }

    @Autowired
    ZWaveRepository zWaveRepository;
    
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


    
}
