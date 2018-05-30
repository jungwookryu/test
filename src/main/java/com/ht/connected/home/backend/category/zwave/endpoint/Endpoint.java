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

import org.springframework.context.annotation.DependsOn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.category.zwave.Zwave;
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

    @Column(name = "specific")
    @JsonProperty("specific")
    String specific;

    @Column(name = "nickname")
    @JsonProperty("nickname")
    String nickname;

    @Column(name = "cmd_cls")
    @JsonProperty("cmd_cls")
    String cmd_cls;

    @Column(name = "zwave_no")
    @JsonProperty("zwave_no")
    String zwaveNo;
  
    @ManyToOne(optional = false)
    @JoinTable(name = "zwave_endpoint",
      joinColumns = @JoinColumn(name = "endpoint_no"),
      inverseJoinColumns = @JoinColumn(name = "zwave_no"))
    private Zwave zwave;
    
    @OneToMany
    @JoinTable(name = "endpoint_cmdcls",
            joinColumns = @JoinColumn(name="endpoint_no"),
            inverseJoinColumns = @JoinColumn(name="cmdcls_no"))
    List<CmdCls> cmdclss;
    
    public boolean addEndpoints(CmdCls cmdCls) {
        
        if(cmdclss == null)
            cmdclss = new ArrayList();
        return cmdclss.add(cmdCls);
        
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCmd_cls() {
        return cmd_cls;
    }

    public void setCmd_cls(String cmd_cls) {
        this.cmd_cls = cmd_cls;
    }

    public String getZwaveNo() {
        return zwaveNo;
    }

    public void setZwaveNo(String zwaveNo) {
        this.zwaveNo = zwaveNo;
    }

    public Zwave getZwave() {
        return zwave;
    }
    public void setZwave(Zwave zwave) {
        this.zwave = zwave;
    }
    public List<CmdCls> getCmdclss() {
        return cmdclss;
    }

}
