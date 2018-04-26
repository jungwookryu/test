package com.ht.connected.home.backend.model.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.model.entity.Gateway;

/**
 * zwave 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * 
 * @author 구정화
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZwaveRequest {

    @JsonProperty("email")
    private String email;
    
    @JsonProperty("serial")
    private String serialNo;

    @JsonProperty("nodeId")
    private int nodeId;

    @JsonProperty("endpointId")
    private int endpointId;

    @JsonProperty("option")
    private String securityOption;

    private int classKey;

    @JsonProperty("classKey")
    private String sClassKey;
    
    private int commandKey;
    
    @JsonProperty("cmdkey")
    private String sCommandKey;

    @JsonProperty("version")
    private String version;

    @JsonProperty("nodeId")
    private Gateway gateway;
    
    @JsonProperty("set_data")
    private HashMap<String, Object> setData;
    
    /**
     * 경로를 배열로 받을경우 생성자
     * 
     * @param topic
     */
    public ZwaveRequest(String[] topic) {
        this.serialNo = topic[4];
        if( 7 < topic.length ) {
            this.classKey = ByteUtil.getStringtoInt(topic[7]);
            this.commandKey = ByteUtil.getStringtoInt(topic[8]);
        }
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

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(int endpointId) {
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

    /**
     * @return the sClassKey
     */
    public String getsClassKey() {
        return sClassKey;
    }

    /**
     * @param sClassKey the sClassKey to set
     */
    public void setsClassKey(String sClassKey) {
        this.sClassKey = sClassKey;
    }

    /**
     * @return the sCommandKey
     */
    public String getsCommandKey() {
        return sCommandKey;
    }

    /**
     * @param sCommandKey the sCommandKey to set
     */
    public void setsCommandKey(String sCommandKey) {
        this.sCommandKey = sCommandKey;
    }

    /**
     * @return the setData
     */
    public HashMap getSetData() {
        return setData;
    }

    /**
     * @param setData the setData to set
     */
    public void setSetData(HashMap setData) {
        this.setData = setData;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ZwaveRequest [email=" + email + ", serialNo=" + serialNo 
                + ", nodeId=" + nodeId + ", endpointId=" + endpointId 
                + ", securityOption=" + securityOption + ", classKey=" + classKey
                + ", sClassKey=" + sClassKey + ", commandKey=" + commandKey 
                + ", sCommandKey=" + sCommandKey + ", version=" + version 
                + ", gateway=" + gateway + ", setData=" + setData + "]";
    }
    
}