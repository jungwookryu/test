package com.ht.connected.home.backend.sip.message.service;

import static java.util.Objects.isNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.dto.account.info.SipOwnerDeviceDto;
import com.ht.connected.home.backend.sip.message.model.dto.account.info.SipOwnerSharedDeviceDto;
import com.ht.connected.home.backend.sip.message.model.dto.account.info.SipSharedDeviceDto;
import com.ht.connected.home.backend.sip.message.model.entity.SipDevice;
import com.ht.connected.home.backend.sip.message.model.entity.SipShare;
import com.ht.connected.home.backend.sip.message.repository.SipDeviceRepository;
import com.ht.connected.home.backend.sip.message.repository.SipShareRepository;

@Service
public class SipDeviceService {

    @Autowired
    private SipDeviceRepository deviceRepository;
    
    @Autowired
    private SipShareRepository shareRepository;

    /**
     * 시리얼 번호와 비밀번호는 디비에 이미 등록되어있어서 신규로 등록하는 기기 정보가 디비저장 정보와 일치해야한다
     * @param request
     */
    public boolean addDevice(SipMqttRequestMessageDto request) {
        boolean isSuccess = false;
        SipDevice device = deviceRepository.findBySerialNumber(request.getBody().get("deviceNo").toString());
        if (!isNull(device)) {
            device.setOwnerAccount(request.getBody().get("userID").toString());
            device.setSerialNumber(request.getBody().get("deviceNo").toString());
            device.setDeviceNickname(request.getBody().get("deviceAlias").toString());
            device.setUserPassword(request.getBody().get("devicePassword").toString());
            device.setLocLatitude(request.getBody().get("latitude").toString());
            device.setLocLongitude(request.getBody().get("longitude").toString());
            device.setOwnership(request.getBody().get("ownership").toString());
            device.setDeviceStatus(request.getBody().get("registerStatus").toString());
            device.setSipRole(request.getBody().get("userID").toString().replace("@", "^"));
            device.setDeviceType("sdb");
            device = deviceRepository.save(device);
            if(device.getOwnerAccount().equals(request.getBody().get("userID"))) {
                isSuccess = true;    
            }                        
        }
        return isSuccess; 
    }
    
    public void deleteDevice(SipMqttRequestMessageDto request) {
        SipDevice device = deviceRepository.findBySerialNumber(request.getBody().get("deviceNo").toString());
        if (!isNull(device)) {
            deviceRepository.delete(device);
        }
    }
    
    public void shareDevice(String serialNumber, String ownerAccount, String sharedAccount, String strRequestType,
            String sipAor) {
        List<SipDevice> devices = deviceRepository.findBySerialNumberAndOwnerAccount(serialNumber, ownerAccount);
        String acceptDate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        ArrayList<SipShare> shares = new ArrayList<>();
        devices.stream().forEach(device -> {
            SipShare share = new SipShare();
            share.setSerialNumber(device.getSerialNumber());
            share.setDeviceType(device.getDeviceType());
            share.setDeviceNickname(device.getDeviceNickname());
            share.setOwnership("shared");
            share.setOwnerAccount(ownerAccount);
            share.setOwnerNickname(device.getOwnerNickname());
            share.setSharedAccount(sharedAccount);
            share.setDeviceStatus(device.getDeviceStatus());
            share.setSharedStatus("request");
            share.setLocLatitude(device.getLocLatitude());
            share.setLocLongitude(device.getLocLongitude());
            share.setSharedNickname("sharedRequestUserAlias");
            share.setSipAor(sipAor);
            share.setAcceptDate(acceptDate);
            shares.add(share);
        });
        shareRepository.save(shares);
    }
    
    public ArrayList getAccountInfo(String strUserId, String strUserType) {
        ArrayList deviceJsonArray = new ArrayList();
        if (strUserType.equalsIgnoreCase("owner")) {
            List<SipDevice> devices = deviceRepository.findByOwnerAccountAndOwnership(strUserId, "owner");
            if (!isNull(devices)) {
                devices.stream().forEach(device -> {
                    SipOwnerDeviceDto ownerDevice = new SipOwnerDeviceDto(device);
                    deviceJsonArray.add(ownerDevice);    
                });
            }
        } else if (strUserType.equalsIgnoreCase("shared")) {
            List<SipShare> shares = shareRepository.findBySharedAccountAndOwnershipAndSharedStatus(strUserId, "shared",
                    "accept");
            if (!isNull(shares)) {               
                shares.stream().forEach(share -> {
                    SipSharedDeviceDto sharedDevice = new SipSharedDeviceDto(share);
                    deviceJsonArray.add(sharedDevice);    
                });
            }
        } else if (strUserType.equalsIgnoreCase("ownerShared")) {
            List<SipShare> shares = shareRepository.findByOwnerAccountAndOwnership(strUserId, "shared");
            if (!isNull(shares)) {
                shares.stream().forEach(share -> {
                    SipOwnerSharedDeviceDto ownerSharedDevice = new SipOwnerSharedDeviceDto(share);
                    deviceJsonArray.add(ownerSharedDevice);    
                });
            }
        }
        return deviceJsonArray;
    }
    
    /**
     * 기기 등록 여부 검사
     * 
     * @param strSN
     * @return
     */
    public String checkRegisteredDevice(String strSN) {
        String errorCode = "00";
        SipDevice device = deviceRepository.findBySerialNumber(strSN);
        if (isNull(device)) {
            // Not registred device
            errorCode = "01";
        } else if (device.getDeviceStatus().equals("registered")) {
            // Registered device
            errorCode = "02";
        } else if (errorCode.equals("00")) {
            SipShare share = shareRepository.findBySerialNumber(strSN);
            if (!isNull(share)) {
                // Shared device
                errorCode = "03";
            }
        }
        return errorCode;
    }

}
