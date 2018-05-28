package com.ht.connected.home.backend.category.zwave;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive.zwave;

/**
 * zwave 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * @author 구정화
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZwaveRequest extends Zwave{

    @JsonProperty("model")
    private String model;

    @JsonProperty("serial")
    private String serialNo;

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

    @JsonProperty("set_data")
    private HashMap<String, Object> setData;

    /**
     * 경로를 배열로 받을경우 생성자
     * @param topic
     */
    public ZwaveRequest(String[] topic) {
        if (topic.length > 2) {
            this.model = topic[3];
            this.serialNo = topic[4];
            if (CategoryActive.zwave.certi.name().equals(topic[6].toString())) {
                if (7 < topic.length && topic.length > 9) {
                    this.version = topic[7].toString();
                    this.sClassKey = topic[8].toString();
                    this.classKey = ByteUtil.getStringtoInt(topic[7]);
                    this.commandKey = ByteUtil.getStringtoInt(topic[8]);
                    this.sCommandKey = topic[9].toString();
                }
            }
        }
    }

    /**
     * ZwaveController에서 생성할 경우 생성자
     * @param req
     * @param classKey
     * @param commandKey
     * @param version
     */
    public ZwaveRequest(HashMap<String, Object> req, int classKey, int commandKey, String version) {
        this.serialNo = req.getOrDefault("serial","").toString();
        super.setNodeId(Integer.valueOf(req.getOrDefault("nodeId",0).toString()));
        super.setEndpointId(Integer.valueOf(req.getOrDefault("endpointId",0).toString()));
        this.securityOption = req.getOrDefault("option",0).toString();
        this.classKey = classKey;
        this.commandKey = commandKey;
        this.version = version;
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

    public String getSecurityOption() {
        return securityOption;
    }

    public void setSecurityOption(String securityOption) {
        this.securityOption = securityOption;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @param setData the setData to set
     */
    public void setSetData(HashMap<String, Object> setData) {
        this.setData = setData;
    }

    @Override
    public String toString() {
        return "ZwaveRequest ["+", model=" + model + ", serialNo=" + serialNo + ", endpointId=" + getEndpointId() + ", securityOption=" + securityOption
                + ", classKey=" + classKey + ", sClassKey=" + sClassKey + ", commandKey=" + commandKey + ", sCommandKey=" + sCommandKey + ", version=" + version + ", nodeId=" + getNodeId() + ", setData="
                + setData + "]"+super.toString();
    }

}