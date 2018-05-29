package com.ht.connected.home.backend.category.zwave;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;

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
    
    @Column(name = "security")
    @JsonProperty("security")
    String security;
    
    @Column(name = "homeid")
    @JsonProperty("homeid")
    String homeid;
    
    @Column(name = "basic")
    @JsonProperty("basic")
    String basic;
    
    @Column(name = "generic")
    @JsonProperty("generic")
    String generic;
    
    @Column(name = "specific")
    @JsonProperty("specific")
    String specific;
    
    @Column(name = "lib_type")
    @JsonProperty("lib_type")
    String libType;
    
    @Column(name = "proto_ver")
    @JsonProperty("proto_ver")
    String protoVer;
    
    @Column(name = "app_ver")
    @JsonProperty("app_ver")
    String appVer;
    
    @Column(name = "crc_cap")
    @JsonProperty("crc_cap")
    String crcCap;
    
    @Column(name = "s2_key_valid")
    @JsonProperty("s2_key_valid")
    String s2KeyValid;
    
    @Column(name = "s2_grnt_keys")
    @JsonProperty("s2_grnt_keys")
    String s2GrntKeys;
    
    @Column(name = "endpoint")
    @JsonProperty("endpoint")
    String sEndpoint;
    
    @OneToMany
    @JoinTable(name = "zwave_endpoint",
            joinColumns = @JoinColumn(name="zwave_no"),
            inverseJoinColumns = @JoinColumn(name="endpoint_no"))
    List<Endpoint> endpoints;
    
    public boolean addEndpoints(Endpoint endpoint) {
        
        if(endpoints == null)
            endpoints = new ArrayList();
        return endpoints.add(endpoint);
        
    }
    
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


    public List<Endpoint> getEndpoints(){
        return endpoints;
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

    public int getEndpointid() {
        return endpointid;
    }

    public void setEndpointid(int endpointid) {
        this.endpointid = endpointid;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getHomeid() {
        return homeid;
    }

    public void setHomeid(String homeid) {
        this.homeid = homeid;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public String getLibType() {
        return libType;
    }

    public void setLibType(String libType) {
        this.libType = libType;
    }

    public String getProtoVer() {
        return protoVer;
    }

    public void setProtoVer(String protoVer) {
        this.protoVer = protoVer;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getCrcCap() {
        return crcCap;
    }

    public void setCrcCap(String crcCap) {
        this.crcCap = crcCap;
    }

    public String getS2KeyValid() {
        return s2KeyValid;
    }

    public void setS2KeyValid(String s2KeyValid) {
        this.s2KeyValid = s2KeyValid;
    }

    public String getS2GrntKeys() {
        return s2GrntKeys;
    }

    public void setS2GrntKeys(String s2GrntKeys) {
        this.s2GrntKeys = s2GrntKeys;
    }

    public String getsEndpoint() {
        return sEndpoint;
    }

    public void setsEndpoint(String sEndpoint) {
        this.sEndpoint = sEndpoint;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

}
