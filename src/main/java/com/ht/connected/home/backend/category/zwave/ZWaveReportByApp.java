package com.ht.connected.home.backend.category.zwave;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointReportByApp;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZWaveReportByApp {

    @JsonProperty("zwave_no")
    int zwaveNo;

    @JsonProperty("nodeId")
    int nodeId;
    
    @JsonProperty("nickname")
    String nickname;
    
    @JsonProperty("status")
    String status;
    
    @JsonProperty("endpoints")
    List<EndpointReportByApp> endpoints;

    public boolean addEndpoints(EndpointReportByApp endpointReportByApp) {

        if(endpoints == null)
            endpoints = new ArrayList();
        return endpoints.add(endpointReportByApp);
        
    }
    
    public int getZwaveNo() {
        return zwaveNo;
    }

    public void setZwaveNo(int zwaveNo) {
        this.zwaveNo = zwaveNo;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNicname() {
        return nickname;
    }

    public void setNicname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EndpointReportByApp> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<EndpointReportByApp> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public String toString() {
        return "ZWaveReportByApp [zwaveNo=" + zwaveNo + ", nodeId=" + nodeId + ", nicname=" + nickname + ", status=" + status + ", endpoints=" + endpoints + "]";
    }

  }
