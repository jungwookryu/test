package com.ht.connected.home.backend.pushwise.model.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 홈 보안 테이블 엔터티 NamedQueries 정의
 * 
 * @author 구정화
 *
 */
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "HomeSecurityNotifyMapping", classes = {
                @ConstructorResult(targetClass = PWHomeSecurity.class, columns = {
                        @ColumnResult(name = "homeNo", type = Integer.class),
                        @ColumnResult(name = "securityStatus", type = Integer.class),
                        @ColumnResult(name = "homeNickname", type = String.class),
                        @ColumnResult(name = "zwaveNickname", type = String.class),
                        @ColumnResult(name = "gatewaySerial", type = String.class) }) }),
        @SqlResultSetMapping(name = "HomeSecurityMapping", classes = {
                @ConstructorResult(targetClass = PWHomeSecurity.class, columns = {
                        @ColumnResult(name = "seq", type = Integer.class),
                        @ColumnResult(name = "updatedAt", type = String.class),
                        @ColumnResult(name = "homeNo", type = Integer.class),
                        @ColumnResult(name = "securityStatus", type = Integer.class),
                        @ColumnResult(name = "homeNickname", type = String.class) }) }) })
@NamedNativeQueries({
        @NamedNativeQuery(name = "PWHomeSecurity.getHomeSecurityGatewaySerial", query = "SELECT g.home_no homeNo, hs.security_status securityStatus, h.nickname homeNickname, z.nickname zwaveNickname, g.serial gatewaySerial "
                + "FROM home_security hs INNER JOIN gateway g ON hs.home_no = g.home_no "
                + "INNER JOIN home h on h.no = hs.home_no "
                + "INNER JOIN zwave z ON g.no = z.gateway_no where z.no=:zwaveNo", resultSetMapping = "HomeSecurityNotifyMapping"),
        @NamedNativeQuery(name = "PWHomeSecurity.getHomeByHomeNo", query = "SELECT hs.seq, hs.updated_at updatedAt, h.no homeNo, "
                + "hs.security_status securityStatus, h.nickname homeNickname "
                + "FROM home h left JOIN home_security hs on h.no = hs.home_no "
                + "where h.no=:homeNo group by h.no", resultSetMapping = "HomeSecurityMapping") })

@Entity
@Table(name = "home_security")
public class PWHomeSecurity {

    @Id
    @Column(name = "seq")
    @GeneratedValue
    private Integer seq;

    @Column(name = "home_no")
    private Integer homeNo;

    @Column(name = "security_status")
    private Integer securityStatus = 3;

    @Column(name = "updated_at")
    private String updatedAt;

    @Transient
    private String homeNickname;

    @Transient
    private String zwaveNickname;

    @Transient
    private String gatewaySerial;

    public PWHomeSecurity(Integer homeNo, Integer securityStatus, String homeNickname, String zwaveNickname,
            String gatewaySerial) {
        this.homeNo = homeNo;
        this.securityStatus = securityStatus;
        this.homeNickname = homeNickname;
        this.zwaveNickname = zwaveNickname;
        this.gatewaySerial = gatewaySerial;
    }

    public PWHomeSecurity(Integer seq, String updatedAt, Integer homeNo, Integer securityStatus, String homeNickname) {
        this.seq = seq;
        this.updatedAt = updatedAt;
        this.homeNo = homeNo;
        this.securityStatus = securityStatus;
        this.homeNickname = homeNickname;
    }

    public PWHomeSecurity() {
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getHomeNo() {
        return homeNo;
    }

    public void setHomeNo(Integer homeNo) {
        this.homeNo = homeNo;
    }

    public Integer getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(Integer securityStatus) {
        this.securityStatus = securityStatus;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getHomeNickname() {
        return homeNickname;
    }

    public void setHomeNickname(String homeNickname) {
        this.homeNickname = homeNickname;
    }

    public String getZwaveNickname() {
        return zwaveNickname;
    }

    public void setZwaveNickname(String zwaveNickname) {
        this.zwaveNickname = zwaveNickname;
    }

    public String getGatewaySerial() {
        return gatewaySerial;
    }

    public void setGatewaySerial(String gatewaySerial) {
        this.gatewaySerial = gatewaySerial;
    }

}
