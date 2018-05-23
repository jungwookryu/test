package com.ht.connected.home.backend.model.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.model.dto.Endpoint;

@Entity
@Table(name = "zwave")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Zwave {

    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("no")
    int no;

    @Column(name = "gateway_no")
    @JsonProperty("gateway_no")
    int gatewayNo;

    @Column(name = "node_id")
    @JsonProperty("nodeId")
    int nodeId;
    
    @JsonProperty("endpointId")
    @Column(name = "endpoint_id")
    int endpointid;

    @Column(name = "nickname")
    String nickname;

    @Column(name = "cmd")
    String cmd;

    @Column(name = "event")
    String event;

    @Column(name = "status")
    String status;

    @Column(name = "crerated_time")
    Date creratedTime;

    @Column(name = "lastmodified_date")
    Date lastModifiedDate;
    
    @Column(name = "endpoint")
    @JsonProperty("endpoint")
    String sEndpoint;
    
    @Transient
    List<Endpoint> endpoint;
    
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getGatewayNo() {
        return gatewayNo;
    }

    public void setGatewayNo(int gatewayNo) {
        this.gatewayNo = gatewayNo;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getEndpointId() {
        return endpointid;
    }

    public void setEndpointId(int endpointId) {
        this.endpointid = endpointId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreratedTime() {
        return creratedTime;
    }

    public void setCreratedTime(Date creratedTime) {
        this.creratedTime = creratedTime;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<Endpoint> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<Endpoint> endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @return the sndpoint
     */
    public String getSndpoint() {
        return sEndpoint;
    }

    /**
     * @param sndpoint the sndpoint to set
     */
    public void setSndpoint(String sndpoint) {
        this.sEndpoint = sndpoint;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Zwave [no=" + no + ", gatewayNo=" + gatewayNo + ", nodeId=" + nodeId + ", endpointId=" + endpointid + ", nickname=" + nickname + ", cmd=" + cmd + ", event=" + event + ", status="
                + status + ", creratedTime=" + creratedTime + ", lastModifiedDate=" + lastModifiedDate + ", endpoint=" + sEndpoint + "]";
    }

}
