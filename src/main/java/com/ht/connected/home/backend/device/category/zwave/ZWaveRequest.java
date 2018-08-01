package com.ht.connected.home.backend.device.category.zwave;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.device.category.CategoryActive;

/**
 * zwave 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * @author 구정화
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZWaveRequest extends ZWave{

    @JsonProperty("target")
    private String target;

    @JsonProperty("source")
    private String source;
    
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

    private String categoryActive;
    /**
     * 경로를 배열로 받을경우 생성자
     * @param topic
     */
    public ZWaveRequest(String[] topic) {
        if (topic.length > 2) {
            this.source = topic[1];
            this.model = topic[3];
            this.serialNo = topic[4];
            this.categoryActive = topic[6];
            if (CategoryActive.zwave.certi.name().equals(categoryActive)) {
                if (7 < topic.length) {
                    this.classKey = ByteUtil.getStringtoInt(topic[7]);
                    this.sClassKey = topic[7].toString();
                    if (8 < topic.length) {
                        this.sCommandKey = topic[8].toString();
                        this.commandKey = ByteUtil.getStringtoInt(topic[8]);
                        if (9 < topic.length) {
                            this.version = topic[9].toString();
                            if (10 < topic.length) {
                                this.nodeId = ByteUtil.getStringtoInt(topic[10].toString());
                                if (11 < topic.length) {
                                    this.endpointId = ByteUtil.getStringtoInt(topic[11].toString());
                                }
                            }
                        }
                        
                    }
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
    public ZWaveRequest(HashMap<String, Object> req, int classKey, int commandKey, String version) {
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the categoryActive
     */
    public String getCategoryActive() {
        return categoryActive;
    }

    /**
     * @param categoryActive the categoryActive to set
     */
    public void setCategoryActive(String categoryActive) {
        this.categoryActive = categoryActive;
    }

}
