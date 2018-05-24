package com.ht.connected.home.backend.emergency;

import java.util.List;

public interface UserEmergencyService {
    List getUserEmergency(String authUserEmail, int GatewayNo);
    List getUserEmergency(String authUserEmail);
    int delete(String authUserEmail, int GatewayNo);
    void delete(int no);
    UserEmergency register(UserEmergency userEmergency);
    UserEmergency modify(int no, UserEmergency userEmergency);
    boolean getExistUserEmergency(String authUserEmail);
}
