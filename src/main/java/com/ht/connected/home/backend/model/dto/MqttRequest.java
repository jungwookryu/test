package com.ht.connected.home.backend.model.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.Zwave;

/**
 * zwave 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * 
 * @author 구정화
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttRequest {

    @JsonProperty("target")
    private String target;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("serial")
    private String serialNo;

    @JsonProperty("model")
    private String model;
    
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
    
    public MqttRequest() {
    }
    public MqttRequest(Gateway gateway) {
        this.serialNo = gateway.getSerial();
        this.model= gateway.getModel();
    }
    
    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param setData the setData to set
     */
    public void setSetData(HashMap<String, Object> setData) {
        this.setData = setData;
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

    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    /**
     * @return the setData
     */
    public HashMap getSetData() {
        return setData;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
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
                + ", sCommandKey=" + sCommandKey + ", version=" + version  + ", model=" + model
                + ", gateway=" + gateway + ", setData=" + setData + ", target=" + target+  "]";
    }
    
}