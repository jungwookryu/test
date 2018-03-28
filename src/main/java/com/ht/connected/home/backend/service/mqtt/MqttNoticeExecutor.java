package com.ht.connected.home.backend.service.mqtt;

import static java.util.Objects.isNull;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.model.dto.MqttNoticePayload;
import com.ht.connected.home.backend.model.entity.Gateway;


/**
 * 호스트 Manager 컨트롤러 Notice Payload 메세지 바디 처리 로직
 * @author 구정화
 *
 */
@Service
@Scope(value = "prototype")
public class MqttNoticeExecutor implements MqttPayloadExecutor {

  /**
   * 호스트 등록/부팅 메세지 executor
   */
  @Override
  public Object execute(MessageArrivedComponent mqttTopicHandler, Gateway gateway)
      throws Exception {
    
    MqttNoticePayload payload = (MqttNoticePayload) mqttTopicHandler
        .getPayload();
    if (payload.getType().equals(MqttNoticePayload.register) && isNull(gateway)) {
      gateway = new Gateway();
      updateHost(payload, gateway, mqttTopicHandler.getSerial());

    } else if (payload.getType().equals(MqttNoticePayload.boot) && !isNull(gateway)) {
      updateHost(payload, gateway, mqttTopicHandler.getSerial());
    }
    return null;
  }

  private void updateHost(MqttNoticePayload payload, Gateway gateway, String serial) {  	

  }
}
