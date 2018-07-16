package com.ht.connected.home.backend.controller.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mqtt")
public class ProducerComponent {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProducerComponent.class);

    /**
     * mqtt message publish 한는 component restful로 접근 가능함.
     * @param message
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/")
    public ResponseEntity run(@RequestBody Message message) throws InterruptedException {

        logger.debug("Sending message... Start");
        String recvMessage = (String) rabbitTemplate.convertSendAndReceive("amq.topic", message.getMessageType(), message.getMessageBody());
        return new ResponseEntity(recvMessage, HttpStatus.OK);

    }

}
