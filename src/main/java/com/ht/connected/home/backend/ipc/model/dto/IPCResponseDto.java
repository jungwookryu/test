package com.ht.connected.home.backend.ipc.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 응답형식 변경에 대비한 클래스 정의
 * 
 * @author 구정화
 *
 */
public class IPCResponseDto {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "msg")
    private String msg;

    @JsonProperty(value = "data")
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
