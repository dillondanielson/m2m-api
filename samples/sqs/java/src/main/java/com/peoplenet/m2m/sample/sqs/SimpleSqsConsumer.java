package com.peoplenet.m2m.sample.sqs;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.BatchResultErrorEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import java.util.ArrayList;
import java.util.List;

public class SimpleSqsConsumer implements Runnable {

	private final String queueUrl;

	private final AmazonSQSClient sqs;

	public SimpleSqsConsumer(String queueUrl) {

		this.queueUrl = queueUrl;
		this.sqs = new AmazonSQSClient(new DefaultAWSCredentialsProviderChain().getCredentials());
	}

	public void run() {

		System.out.println("Starting simple consumer");
		while (true) {
			try {
				consumeNextBatch();
			} catch (QueueDoesNotExistException e) {
				System.out.println("Queue does not exist for queueUrl:" + queueUrl);
				throw e;
			} catch (Throwable t) {
				System.out.println("Error attempting to consume from queueUrl:" + queueUrl);
				t.printStackTrace();
				System.out.println("Waiting 5 seconds before retry.");
				sleep(5000);
			}
		}
	}

	private void consumeNextBatch() {

		System.out.println("Checking for new messages...");

		// Receive messages, note by default PeopleNet queues are setup to long poll for up to 20 seconds.
		// This call will block until messages are available or the long polling interval expires.
		ReceiveMessageResult result = sqs.receiveMessage(new ReceiveMessageRequest().withQueueUrl(queueUrl).withMaxNumberOfMessages(10));

		if (result.getMessages().size() > 0) {

			// Process messages
			List<DeleteMessageBatchRequestEntry> deleteEntries = new ArrayList<DeleteMessageBatchRequestEntry>();
			int msgId = 0;
			for (Message message : result.getMessages()) {
				System.out.println("Received message: " + message.getBody());
				// additional processing logic goes here
				deleteEntries.add(new DeleteMessageBatchRequestEntry().withReceiptHandle(message.getReceiptHandle()).withId(Integer.toString(msgId)));
				msgId++;
			}

			// Delete (ack) messages in batch
			DeleteMessageBatchResult deleteResult = sqs.deleteMessageBatch(new DeleteMessageBatchRequest().withQueueUrl(queueUrl).withEntries(deleteEntries));

			// Log failed deletes
			for (BatchResultErrorEntry error : deleteResult.getFailed()) {
				System.out.println(error.getMessage());
			}
		} else {
			System.out.println("No messages available");
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
