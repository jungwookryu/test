package com.ht.connected.home.backend.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Override
    public String toString() {
        return "Endpoint [no=" + no + ", epid=" + epid + ", generic=" + generic + ", specific=" + specific + ", nickname=" + nickname + ", cmd_cls=" + cmd_cls + "]";
    }
    

}
