package com.ht.connected.home.backend.category.zwave.cmdcls;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;

@Entity
@Table(name = "cmd_cls")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmdCls {

    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("no")
    int no;

    @Column(name = "secure")
    @JsonProperty("secure")
    String secure;

    @Column(name = "cmd_class")
    @JsonProperty("cmd_class")
    String cmdClass;

    @Column(name = "ver")
    @JsonProperty("ver")
    String ver;

    @Column(name = "real_ver")
    @JsonProperty("real_ver")
    String realVer;

    @Column(name = "rpt_cmd")
//    @JsonProperty("rpt_cmd")
    String rptCmd;

    @Column(name = "endpoint_no")
    @JsonProperty("endpoint_no")
    int endpointNo;

    @ManyToOne(optional = false)
    @JoinTable(name = "endpoint_cmdcls",
        joinColumns = @JoinColumn(name = "cmdcls_no"),
        inverseJoinColumns = @JoinColumn(name = "endpoint_no"))
    private Endpoint endpoint;

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

    public String getCmdClass() {
        return cmdClass;
    }

    public void setCmdClass(String cmdClass) {
        this.cmdClass = cmdClass;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getRealVer() {
        return realVer;
    }

    public void setRealVer(String realVer) {
        this.realVer = realVer;
    }

    public String getRptCmd() {
        return rptCmd;
    }

    public void setRptCmd(String rptCmd) {
        this.rptCmd = rptCmd;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getEndpointNo() {
        return endpointNo;
    }

    public void setEndpointNo(int endpointNo) {
        this.endpointNo = endpointNo;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }
}
