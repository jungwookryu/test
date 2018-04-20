package com.ht.connected.home.backend.model.dto;

import java.util.HashMap;

import com.ht.connected.home.backend.model.entity.Gateway;

/**
 * zwave 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * 
 * @author 구정화
 *
 */

public class ZwaveRequest {

    private String email;

    private String serialNo;

    private Integer nodeId;

    private Integer endpointId;

    private String securityOption;

    private int classKey;

    private int commandKey;

    private String version;

    private Gateway gateway;

    /**
     * 경로를 배열로 받을경우 생성자
     * 
     * @param topic
     */
    public ZwaveRequest(String[] topic) {
        this.serialNo = topic[4];
        this.classKey = Integer.parseInt(topic[7]);
        this.commandKey =Integer.parseInt(topic[8]);
    }

    /**
     * ZwaveController에서 생성할 경우 생성자
     * 
     * @param req
     * @param classKey
     * @param commandKey
     * @param version
     */
    public ZwaveRequest(HashMap<String, Object> req, int classKey, int commandKey, String version) {      
        this.serialNo = req.get("serial").toString();
        this.nodeId = Integer.valueOf(req.get("nodeId").toString());
        this.endpointId = Integer.valueOf(req.get("endpointId").toString());
        this.securityOption = req.get("option").toString();
        this.classKey = classKey;
        this.commandKey = commandKey;
        this.version = version;     
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public int getClassKey() {
        return classKey;
    }

    public int getCommandKey() {
        return commandKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Integer endpointId) {
        this.endpointId = endpointId;
    }

    public String getSecurityOption() {
        return securityOption;
    }

    public void setSecurityOption(String securityOption) {
        this.securityOption = securityOption;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setClassKey(int classKey) {
        this.classKey = classKey;
    }

    public void setCommandKey(int commandKey) {
        this.commandKey = commandKey;
    }

}