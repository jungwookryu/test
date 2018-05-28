package com.ht.connected.home.backend.category.ir;

import java.util.List;

import org.eclipse.jetty.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.service.mqtt.RequestBase;

/**
 * ir 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * 
 * @author injeongLee
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IRRequest extends RequestBase{
    
    @JsonProperty(value = "type")
    private String type;
    
    
    @JsonProperty(value = "format")
    private String format;
    
    @JsonProperty(value = "rptcnt")
    private int rptcnt;
    
    @JsonProperty(value = "gap")
    private int gap;
    
    @JsonProperty(value = "value")
    private List<IRValue> value;
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }


    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }


    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }


    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }


    /**
     * @return the rptcnt
     */
    public int getRptcnt() {
        return rptcnt;
    }


    /**
     * @param rptcnt the rptcnt to set
     */
    public void setRptcnt(int rptcnt) {
        this.rptcnt = rptcnt;
    }


    /**
     * @return the gap
     */
    public int getGap() {
        return gap;
    }


    /**
     * @param gap the gap to set
     */
    public void setGap(int gap) {
        this.gap = gap;
    }


    /**
     * @return the value
     */
    public List<IRValue> getValue() {
        return value;
    }


    /**
     * @param value the value to set
     */
    public void setValue(List<IRValue> value) {
        this.value = value;
    }
    
    public IRRequest(String topic) {
        super(topic);
    }

    public IRRequest(String topic, String payload) throws JSONException {
        super(topic);
        JSONObject jsonObj =new JSONObject();
        if(StringUtil.isNotBlank(payload)) {
            jsonObj = new JSONObject(payload);
        }
        this.type = jsonObj.getString("type");
        this.gap = jsonObj.getInt("gap");
        this.rptcnt = jsonObj.getInt("rptcnt");
        this.value = (List<IRValue>) jsonObj.getJSONArray("value");
    }
}