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


    @Column(name = "zwave_no")
    @JsonProperty("zwave_no")
    int zwaveNo;
  
    @Column(name = "cmd_cls")
    String cmdCls;
    
    
    @JsonProperty("cmd_cls")
    @Transient
    List<CmdCls> cmdClses;
    
    @ManyToOne(optional = false)
    @JoinTable(name = "zwave_endpoint",
      joinColumns = @JoinColumn(name = "zwave_no"),
      inverseJoinColumns = @JoinColumn(name = "no"))
    private ZWave zwave;
    
    @OneToMany
    @JoinTable(name = "endpoint_cmd_cls",
            joinColumns = @JoinColumn(name="no"),
            inverseJoinColumns = @JoinColumn(name="endpoint_no"))
    List<CmdCls> cmdClss;
    
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

    public ZWave getZwave() {
        return zwave;
    }
    public void setZwave(ZWave zwave) {
        this.zwave = zwave;
    }
    public List<CmdCls> getCmdClses() {
        return cmdClses;
    }
    public void setCmdClses(List<CmdCls> cmdClses) {
        this.cmdClses = cmdClses;
    }
    public List<CmdCls> getCmdClss() {
        return cmdClss;
    }
    public void setCmdClss(List<CmdCls> cmdClss) {
        this.cmdClss = cmdClss;
    }
    public String getCmdCls() {
        return cmdCls;
    }

}
