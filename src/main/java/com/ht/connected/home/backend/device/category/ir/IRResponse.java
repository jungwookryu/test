package com.ht.connected.home.backend.device.category.ir;

import static java.util.Objects.isNull;

import org.eclipse.jetty.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.service.mqtt.RequestBase;
import com.ht.connected.home.backend.service.mqtt.Response;

/**
 * ir 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * 
 * @author injeongLee
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IRResponse extends RequestBase{
    
    @JsonProperty(value = "type")
    private String type;
    
    @JsonProperty(value = "response")
    private Response response;
    
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


    public IRResponse(String topic) {
        super(topic);
    }

    public IRResponse(String topic, String payload) throws JSONException {
        super(topic);
        JSONObject jsonObj =new JSONObject();
        if(StringUtil.isNotBlank(payload)) {
            jsonObj = new JSONObject(payload);
        }
        Object type = jsonObj.get("type");
        if(!isNull(type)) {
            this.type = jsonObj.getString("type");
        }
        Object response = jsonObj.get("response");
        if(!isNull(type)) {
//            this.response = jsonObj.getJSONObject("response");
        }
    }
}