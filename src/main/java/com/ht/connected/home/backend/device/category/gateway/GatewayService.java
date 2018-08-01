package com.ht.connected.home.backend.device.category.gateway;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.ht.connected.home.backend.device.category.gateway.gatewayCategory.GatewayCategory;
import com.ht.connected.home.backend.user.User;

public interface GatewayService{
    List getGatewayList(String status,String authUserEmail);
    void delete(int no) throws InterruptedException;
    void deleteCategory(GatewayCategory gatewayCategory);
    Gateway modifyGateway(Gateway originGateway, Gateway gateway);
    Gateway findOne(int no);
    boolean shareGateway(String mode , Gateway originGateway, User user);
    void subscribe(String topic, String payload) throws JsonParseException, JsonMappingException, IOException, InterruptedException;
    void hostReset(String serial);
}
