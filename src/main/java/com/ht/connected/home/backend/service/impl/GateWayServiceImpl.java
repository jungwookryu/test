package com.ht.connected.home.backend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.UserGateway;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.repository.UserGatewayRepository;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.GateWayService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class GateWayServiceImpl extends CrudServiceImpl<Gateway , Integer> implements GateWayService{
	
	private GateWayRepository gateWayRepository;

	@Autowired
	public GateWayServiceImpl(GateWayRepository gateWayRepository) {
		super(gateWayRepository);
		this.gateWayRepository = gateWayRepository;
	}
	
    @Autowired
    UsersRepository userRepository;

    @Autowired
    GateWayRepository gatewayRepository;

    @Autowired
    UserGatewayRepository userGatewayRepository;
    
    public List getGatewayList(String authUserEmail) {
        List<Gateway> lstGateways = new ArrayList<>();
        List<Users> users = userRepository.findByUserEmail(authUserEmail);
        Users user = users.get(0);
        List<UserGateway> userGateways = userGatewayRepository.findByUserNo(user.getNo());

        List<Integer> gatewayNos = new ArrayList<Integer>();
        userGateways.stream().forEach(userGateway -> gatewayNos.add(userGateway.getGatewayNo()));
        List<Gateway> gateways = gatewayRepository.findAll(gatewayNos);
        List<UserGateway> userGatewayList = userGatewayRepository.findByGatewayNoIn(gatewayNos);

        gateways.forEach(gateway -> {
            Gateway aGateway = new Gateway();
            aGateway.setSerial(gateway.getSerial());
            aGateway.setModel(gateway.getModel());
            aGateway.setNickname(gateway.getNickname());
            Users master = getMasterUserNicknameByGatewayNo(userGatewayList, gateway.getNo());
            aGateway.setUserNickname(master.getNickName());
            aGateway.setUserEmail(master.getUserEmail());
            lstGateways.add(aGateway);
        });
        return lstGateways;
    }
    private Users getMasterUserNicknameByGatewayNo(List<UserGateway> userGatewayList, Integer gatewayNo) {      
        UserGateway userGateway = userGatewayList.stream()
                .filter(ug -> ug.getGroupRole().equals("master") && gatewayNo.equals(ug.getGatewayNo()))
                .collect(Collectors.toList()).get(0);
        Users user = userRepository.findOne(userGateway.getUserNo());
        return user;
    }

}
