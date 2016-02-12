package com.peoplenet.m2m.sample.sqs;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClient;

/**
 * Sends messages to the specified queue.
 */
public class SimpleSqsSender implements Runnable {

	private final String queueUrl;

	private final AmazonSQSClient sqs;

	public SimpleSqsSender(String queueUrl) {

		this.queueUrl = queueUrl;
		this.sqs = new AmazonSQSClient(new DefaultAWSCredentialsProviderChain().getCredentials());
	}

	/**
	 * Sends messages in a loop delaying 1 second between each message.
	 */
	public void run() {

		System.out.println("Sending messages...");
		int count = 0;
		while (true) {
			String payload = "{\"counter\":" + count + "}";
			sqs.sendMessage(queueUrl, payload);
			count++;
			sleep(1000);
		}
	}

	private void sleep(long millis) {

		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
