package com.ht.connected.home.backend.device.category.zwave.cmdcls;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "cmd_cls",
uniqueConstraints={
    @UniqueConstraint(
        columnNames={"endpoint_no","cmd_class"}
    )
})
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
    String rptCmd;
    
    @JsonProperty("rpt_cmd")
    @Transient
    String[] rptCmds;
    
    @Column(name = "endpoint_no")
    @JsonProperty("endpoint_no")
    int endpointNo;

    public boolean addRptCmd(String sRptCmd) {
        if(rptCmds == null) {
            rptCmds = new String[0];
        }
        rptCmds = sRptCmd.split(","); 
        return true;
    }
    
    public String getSrptCmd(String[] rptCmds) {
        if(rptCmds == null) {
            rptCmds = new String[0];
        }
        return Arrays.toString(rptCmds);
    }
    
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

    public String[] getRptCmds() {
        return rptCmds;
    }

    public void setRptCmds(String[] rptCmds) {
        this.rptCmds = rptCmds;
    }
}
