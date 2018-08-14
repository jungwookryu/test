package com.ht.connected.home.backend.pushwise.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 푸시 기록 테이블 엔터티 정의
 * 
 * @author 구정화
 *
 */
@Entity
@Table(name = "push_history")
public class PWPushHistory {

    @Id
    @Column(name = "seq")
    @GeneratedValue
    private int seq;

    @Column(name = "msg_code")
    private String messageCode;

    @Column(name = "msg")
    private String message;

    @Column(name = "push_type")
    private String pushType;

    @Column(name = "created_at")
    private String createdAt;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
