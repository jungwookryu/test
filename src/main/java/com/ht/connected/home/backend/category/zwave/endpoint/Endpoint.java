package com.ht.connected.home.backend.category.zwave.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.DependsOn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdCls;

@Entity
@Table(name = "endpoint")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Endpoint {

    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("no")
    int no;

    @Column(name = "epid")
    @JsonProperty("epid")
    int epid;

    @Column(name = "generic")
    @JsonProperty("generic")
    String generic;

    @Column(name = "`specific`")
    @JsonProperty("specific")
    String specific;
    
    @Column(name = "nickname")
    @JsonProperty("nickname")
    String nickname;

    @Column(name = "zwave_no")
    @JsonProperty("zwave_no")
    int zwaveNo;
  
    @Column(name = "cmd_cls")
    String cmdCls;
    
    @JsonProperty("status")
    @Column(name = "status")
    int status;

    @JsonProperty("device_functions")
    @Column(name = "device_functions")
    String deviceFunctions;
    
    @JsonProperty("device_type")
    @Column(name = "device_type")
    String deviceType;
    
    @JsonProperty("device_nickname")
    @Column(name = "device_nickname")
    String deviceNickname;
    
    @JsonProperty("input_status")
    @Column(name = "input_status")
    String inputStatus;
    
    @JsonProperty("output_status")
    @Column(name = "output_status")
    String outputStatus;
    
    @JsonProperty("cmd_cls")
    @Transient
    List<CmdCls> cmdClses;
    
    public boolean addEndpoints(CmdCls cmdCls) {
        
        if(cmdClses == null) {
            cmdClses = new ArrayList();
        }
        return cmdClses.add(cmdCls);
        
    }
    
    public String getScmdClses(List<CmdCls> cmdClses) {
        String rtnString="[";
        if(cmdClses == null) {
            return "]";
        }
        for (int i = 0; i < cmdClses.size(); i++) {
            rtnString += cmdClses.get(i).getCmdClass() +",";    
        }
        
        return rtnString + "]";
        
    }
    
    public String getScmdClses() {
        return cmdCls;
    }
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getEpid() {
        return epid;
    }

    public void setEpid(int epid) {
        this.epid = epid;
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

    public int getZwaveNo() {
        return zwaveNo;
    }

    public void setZwaveNo(int zwaveNo) {
        this.zwaveNo = zwaveNo;
    }

    public void setCmdCls(String cmdCls) {
        this.cmdCls = cmdCls;
    }

    public List<CmdCls> getCmdClses() {
        return cmdClses;
    }
    public void setCmdClses(List<CmdCls> cmdClses) {
        this.cmdClses = cmdClses;
    }
    public String getCmdCls() {
        return cmdCls;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeviceFunctions() {
        return deviceFunctions;
    }

    public void setDeviceFunctions(String deviceFunctions) {
        this.deviceFunctions = deviceFunctions;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceNickname() {
        return deviceNickname;
    }

    public void setDeviceNickname(String deviceNickname) {
        this.deviceNickname = deviceNickname;
    }

    public String getInputStatus() {
        return inputStatus;
    }

    public void setInputStatus(String inputStatus) {
        this.inputStatus = inputStatus;
    }

    public String getOutputStatus() {
        return outputStatus;
    }

    public void setOutputStatus(String outputStatus) {
        this.outputStatus = outputStatus;
    }

}
