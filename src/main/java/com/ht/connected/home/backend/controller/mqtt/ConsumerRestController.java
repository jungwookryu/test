package com.ht.connected.home.backend.controller.mqtt;

import com.ht.connected.home.backend.model.rabbit.consumer.Consumer;
import com.ht.connected.home.backend.model.rabbit.consumer.ConsumerBuilder;
import com.ht.connected.home.backend.model.rabbit.consumer.MessageLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerRestController {

    @Autowired
    private ConsumerBuilder consumerBuilder;

    private MessageLogger messageLogger = MessageLogger.getInstance();

    private Map<String, Consumer> consumerPool = new HashMap<String, Consumer>();

    @RequestMapping(path = "/consumer/register/{consumerName}/{queueName}/{routingKey}", method = RequestMethod.POST)
    String register(
            @PathVariable String consumerName,
            @PathVariable String queueName,
            @PathVariable String routingKey,
            @RequestParam(value = "faultyConsumer", required = false, defaultValue = "false") boolean faultyConsumer,
            @RequestParam(value = "runtime", required = false, defaultValue = "0") int runtime
    ) {
        consumerPool.put(consumerName, consumerBuilder.withFaultyReceiver(faultyConsumer).withRuntime(runtime).build(consumerName, routingKey, queueName));
        messageLogger.log("Registered new consumer: " + consumerName);
        return "Registered new consumer: " + consumerName;
    }
    
    @RequestMapping(path = "/{requestDevice}/{AccessDevice}/{classKey}/{commandKey}/{versionInfo}", method = RequestMethod.POST)
    String getList(
    		@PathVariable String requestDevice,
    		@PathVariable String AccessDevice,
            @PathVariable String classKey,
            @PathVariable String commandKey,
            @PathVariable String versionInfo,
            @PathVariable String zwave,
            @RequestParam(value = "user_email", required = false, defaultValue = "guest") boolean userEmail,
            @RequestParam(value = "serialNo", required = false, defaultValue = "guest") boolean serialNo,
            @RequestParam(value = "nodeId", required = false, defaultValue = "nodeId") boolean nodeId,
            @RequestParam(value = "endpointId", required = false, defaultValue = "endpointId") boolean endpointId,
            @RequestParam(value = "option", required = false, defaultValue = "option") boolean option
    ) {
        consumerPool.put(classKey, consumerBuilder.withFaultyReceiver(true).withRuntime(0).build(classKey, commandKey, versionInfo));
        messageLogger.log("Registered new classKey: " + classKey);
        return "Registered new consumer: " + userEmail;
    }
    
    @RequestMapping(path = "/", method = RequestMethod.GET)
    List<String> messages() {
        return messageLogger.getLoggedMessages();
    }

}