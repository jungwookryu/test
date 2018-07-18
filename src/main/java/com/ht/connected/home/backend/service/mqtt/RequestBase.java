package com.ht.connected.home.backend.service.mqtt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ir 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * 
 * @author injeongLee
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract public class RequestBase{
    
    @JsonProperty(value = "source")
    String source;
    
    @JsonProperty(value = "target")
    String target;
    
    @JsonProperty(value = "model")
    String model;
    
    @JsonProperty(value = "serial")
    String serial;
    
    @JsonProperty(value = "category")
    String category;
    
    //  /{source}/{target}/{model}/{serial}/{category}
    public RequestBase(String topic) {
        String[] splitTopic = topic.split(".");
        this.source= splitTopic[1];
        this.target= splitTopic[2];
        this.model= splitTopic[3];
        this.serial= splitTopic[4];
        this.category= splitTopic[5];
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }


    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }


    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }


    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }


    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }


    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }


    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }


    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }


    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }


    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RequestBase [source=" + source + ", target=" + target + ", model=" + model + ", serial=" + serial + ", category=" + category + "]";
    }

    
}