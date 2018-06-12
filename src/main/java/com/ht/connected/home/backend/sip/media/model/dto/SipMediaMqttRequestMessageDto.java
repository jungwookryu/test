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

    @JsonProperty(value = "Event")
    private String event;

    @JsonProperty(value = "EventID")
    private String eventId;

    @JsonProperty(value = "TimeStamp")
    private String timestamp;

    @JsonProperty(value = "Data")
    private String data;

    @JsonProperty(value = "FileInfo")
    private FileInfoDto fileInfo;

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

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timeStamp) {
        this.timestamp = timeStamp;
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

    public static class FileInfoDto {

        @JsonProperty(value = "FileExt")
        private String fileExt;

        @JsonProperty(value = "VideoExist")
        private boolean videoExist;

        @JsonProperty(value = "RawFileTotSize")
        private int rawFileTotSize;

        @JsonProperty(value = "NumOfSlot")
        private int numOfSlot;

        @JsonProperty(value = "SlotIndex")
        private int slotIndex;

        @JsonProperty(value = "SlotSize")
        private SlotSizeDto SlotSize;

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

        public int getRawFileTotSize() {
            return rawFileTotSize;
        }

        public void setRawFileTotSize(int rawFileTotSize) {
            this.rawFileTotSize = rawFileTotSize;
        }

        public int getNumOfSlot() {
            return numOfSlot;
        }

        public void setNumOfSlot(int numOfSlot) {
            this.numOfSlot = numOfSlot;
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        public void setSlotIndex(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        public SlotSizeDto getSlotSize() {
            return SlotSize;
        }

        public void setSlotSize(SlotSizeDto slotSize) {
            SlotSize = slotSize;
        }

    }

    public static class SlotSizeDto {

        @JsonProperty(value = "Raw")
        private int raw;

        @JsonProperty(value = "Base64")
        private int base64;

        public int getRaw() {
            return raw;
        }

        public void setRaw(int raw) {
            this.raw = raw;
        }

        public int getBase64() {
            return base64;
        }

        public void setBase64(int base64) {
            this.base64 = base64;
        }

    }

}
