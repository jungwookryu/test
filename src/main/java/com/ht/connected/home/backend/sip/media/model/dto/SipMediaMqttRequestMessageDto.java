package com.ht.connected.home.backend.sip.media.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * MQTT 수신 메세지 데이터 모델 클래스
 * 
 * @author 구정화
 *
 */
@JsonIgnoreProperties({ "serialNumber", "topic" })
public class SipMediaMqttRequestMessageDto {

    private String[] topic;
    private String serialNumber;

    @JsonProperty(value = "EventID")
    private String eventId;

    @JsonProperty(value = "Event")
    private String event;

    @JsonProperty(value = "Data")
    private String data;

    @JsonProperty(value = "FileInfo")
    private FileInfoDto fileInfo;

    private String timestamp;

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic.split("/");
        setSerialNumber(this.topic[1]);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public FileInfoDto getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoDto fileInfo) {
        this.fileInfo = fileInfo;
    }

    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class FileInfoDto {

        @JsonProperty(value = "FileExt")
        private String fileExt;

        @JsonProperty(value = "VideoExist")
        private boolean videoExist;

        public String getFileExt() {
            return fileExt;
        }

        public void setFileExt(String fileExt) {
            this.fileExt = fileExt;
        }

        public boolean isVideoExist() {
            return videoExist;
        }

        public void setVideoExist(boolean videoExist) {
            this.videoExist = videoExist;
        }

    }

}
