package com.ht.connected.home.backend.sip.message.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.dto.SipSharedDeviceDto;
import com.ht.connected.home.backend.sip.message.model.entity.SipShare;
import com.ht.connected.home.backend.sip.message.repository.SipShareRepository;

@Service
public class SipShareService {

	@Autowired
	private SipShareRepository shareRepository;


	public List<SipSharedDeviceDto> getSharedDevices(SipMqttRequestMessageDto request, String sharedRequestUserID) {

		List<SipShare> sharedDevices = shareRepository.findBySharedAccount(sharedRequestUserID);
		
		List<SipSharedDeviceDto> sharedDevicesInfo = new ArrayList<>();
		sharedDevices.stream().forEach(sharedDevice->{
			SipSharedDeviceDto sharedDeviceDto = new SipSharedDeviceDto();
			sharedDeviceDto.setDevSerial(sharedDevice.getSerialNumber());
			sharedDeviceDto.setDevType(sharedDevice.getDeviceType());
			sharedDeviceDto.setOwnerAccount(sharedDevice.getOwnerAccount());
			sharedDeviceDto.setDevAlias(sharedDevice.getDeviceNickname());
			sharedDeviceDto.setSharedStatus(sharedDevice.getSharedStatus());
			sharedDevicesInfo.add(sharedDeviceDto);
		});
		return sharedDevicesInfo;
	}
	
	public boolean updateSharedStatus(String strOwnership, String strStatus, String strDevSerial, String strSharedID) {
        SipShare share = shareRepository.findBySerialNumberAndOwnerAccountAndSharedAccount(strDevSerial, strOwnership, strSharedID);
        String timeStamp = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        share.setSharedStatus(strStatus);
        share.setAcceptDate(timeStamp);
        shareRepository.save(share);       
        return true;
    }

}
