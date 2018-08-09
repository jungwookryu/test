package com.ht.connected.home.backend.device.category.gateway;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.ht.connected.home.backend.device.category.gateway.gatewayCategory.GatewayCategory;

public interface GatewayService{
    void delete(int no) throws InterruptedException;
    void deleteCategory(GatewayCategory gatewayCategory);
    Gateway modifyGateway(Gateway originGateway, Gateway gateway);
    Gateway findOne(int no);
    Gateway findBySerial(String serial);
    void subscribe(String topic, String payload) throws JsonParseException, JsonMappingException, IOException, InterruptedException;
    void hostReset(String serial);
	List getGatewayListByHome(List<Integer> iHomes);
}
