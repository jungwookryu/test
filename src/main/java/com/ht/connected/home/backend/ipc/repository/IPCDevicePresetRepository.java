package com.ht.connected.home.backend.ipc.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.ipc.model.entity.IPCDevicePreset;

/**
 * IPC 프리셋 리파지토리
 * 
 * @author 구정화
 *
 */
@Repository
public interface IPCDevicePresetRepository extends JpaRepository<IPCDevicePreset, Integer> {

    List<IPCDevicePreset> findByDeviceSerial(String string);

    IPCDevicePreset findByDeviceSerialAndPresetId(String string, String presetId);

    @Query(nativeQuery = true)
    List<IPCDevicePreset> getDevicePresets(@Param(value = "deviceSerial") String deviceSerial);

    @Transactional
    void deleteByDeviceSerial(String string);

    @Transactional
    void deleteByDeviceSerialAndChannelNoAndPresetId(String string, String string2, String string3);

}
