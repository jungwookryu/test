package com.ht.connected.home.backend.model.rabbit.consumer;

public class LongRunningReceiver implements Receiver {
	private MessageLogger messageLogger = MessageLogger.getInstance();

	private String consumerName;

	private int runtime;

	public LongRunningReceiver(String consumerName, int runtime) {
		this.consumerName = consumerName;
		this.runtime = runtime;
	}

	public void receiveMessage(String message) throws Exception {
		messageLogger.log("[" + consumerName + "]  sleeping " + Integer.toString(runtime) + " seconds...");
		Thread.sleep(runtime * 1000);
		messageLogger.log("[" + consumerName + "]  " + message);
	}

}
