package com.ht.connected.home.backend.client.emergency;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class UserEmergencyServiceImpl extends CrudServiceImpl<UserEmergency, Integer> implements UserEmergencyService {

    public UserEmergencyServiceImpl(JpaRepository<UserEmergency, Integer> jpaRepository) {
        super(jpaRepository);
    }

    private final static Logger logger = LoggerFactory.getLogger(UserEmergencyServiceImpl.class);

    @Autowired
    UserEmergencyRepository userEmergencyRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List getUserEmergency(String authUserEmail, int GatewayNo) {
        
        List<UserEmergency> userEmergency = userEmergencyRepository.findByUserEmailAndGatewayNo(authUserEmail, GatewayNo);
        return userEmergency;
    }
    
    @Override
    public List getUserEmergency(String authUserEmail) {
        
        List<UserEmergency> userEmergency = userEmergencyRepository.findByUserEmail(authUserEmail);
        return userEmergency;
    }
    
    @Override
    public int delete(String authUserEmail, int gatewayNo) {
        int userEmergency = userEmergencyRepository.deleteByUserEmailAndGatewayNo(authUserEmail, gatewayNo);
        return userEmergency;
    }
    
    
    @Override
    public void delete(int no) {
        delete(no);
    }

    @Override
    public boolean getExistUserEmergency(String authUserEmail) {
        return (getUserEmergency(authUserEmail).size()>0);
    }

    @Override
    public UserEmergency register(UserEmergency userEmergency) {
        UserEmergency rtnUserEmergency= userEmergencyRepository.save(userEmergency);
        return rtnUserEmergency;
    }

    @Override
    public UserEmergency modify(int no, UserEmergency userEmergency) {
        UserEmergency rtnUserEmergency= userEmergencyRepository.save(userEmergency);
        return rtnUserEmergency;
    }

    
}
