package com.ht.connected.home.backend.ipc.model.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 마스터 계정 관리
 * 
 * @author 구정화
 *
 */
@SqlResultSetMapping(name = "AccountDevicePresetMapping", classes = {
        @ConstructorResult(targetClass = IPCDevicePreset.class, columns = {
                @ColumnResult(name = "iot_account", type = String.class),
                @ColumnResult(name = "device_serial", type = String.class),
                @ColumnResult(name = "preset_id", type = String.class),
                @ColumnResult(name = "nickname", type = String.class) }) })
@NamedNativeQuery(name = "IPCDevicePreset.getAccountDevicePreset", query = "select a.iot_account, p.device_serial, p.preset_id, p.nickname "
        + "from ipc_account a left join ipc_device d on a.seq=d.account_seq inner join ipc_device_preset p on d.device_serial=p.device_serial "
        + "where d.device_serial=:deviceSerial and a.iot_account=:iotAccount", resultSetMapping = "AccountDevicePresetMapping")
@Entity
@Table(name = "ipc_device_preset")
public class IPCDevicePreset {

    @Id
    @Column(name = "seq")
    @GeneratedValue
    private int seq;

    @Column(name = "device_serial")
    private String deviceSerial;

    @Column(name = "channel_no")
    private String channelNo;

    @Column(name = "preset_id")
    private String presetId;

    @Column(name = "nickname")
    private String nickname;

    @Transient
    private String iotAccount;

    public IPCDevicePreset() {

    }

    public IPCDevicePreset(String iotAccount, String deviceSerial, String presetId, String nickname) {
        this.deviceSerial = deviceSerial;
        this.presetId = presetId;
        this.nickname = nickname;
        this.iotAccount = iotAccount;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getPresetId() {
        return presetId;
    }

    public void setPresetId(String presetId) {
        this.presetId = presetId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

}
