package com.ht.connected.home.backend.model.rabbit.consumer;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;


public class FaultyReceiver implements Receiver {

	private MessageLogger messageLogger = MessageLogger.getInstance();
	
    private String consumerName;

    public FaultyReceiver(String consumerName) {
        this.consumerName = consumerName;
    }

    public void receiveMessage(String message) throws Exception {
    	messageLogger.log("[" + consumerName + "]  throws exception and does not handle " + message);
        throw new Exception("Receiver Exception");
    }

	@Override
	public void send(MidiMessage message, long timeStamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}