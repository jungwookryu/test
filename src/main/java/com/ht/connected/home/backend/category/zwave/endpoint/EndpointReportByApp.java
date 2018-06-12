package com.ht.connected.home.backend.category.zwave.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndpointReportByApp {
    
    @JsonProperty("endpoint_no")
    int endpointNo;
    
    @JsonProperty("epid")
    int epid;
    
    @JsonProperty("nickname")
    String nickname;
    
    @JsonProperty("ep_status")
    int epStatus;

    public EndpointReportByApp() {
        
    }
    public int getEpid() {
        return epid;
    }

    public int getEndpointNo() {
        return endpointNo;
    }

    public void setEndpointNo(int endpointNo) {
        this.endpointNo = endpointNo;
    }

    public void setEpid(int epid) {
        this.epid = epid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getEpStatus() {
        return epStatus;
    }

    public void setEpStatus(int epStatus) {
        this.epStatus = epStatus;
    }

    @Override
    public String toString() {
        return "ZWaveReportByApp [endpointNo=" + endpointNo + "epid=" + epid + ", nickname=" + nickname + ", ep_status=" + epStatus + "]";
    }
  }
