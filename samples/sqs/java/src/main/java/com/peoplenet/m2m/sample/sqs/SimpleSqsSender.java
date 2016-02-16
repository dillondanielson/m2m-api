package com.peoplenet.m2m.sample.sqs;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.peoplenet.m2m.sample.sqs.metrics.Metrics;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends messages to the specified queue.
 */
public class SimpleSqsSender implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SimpleSqsSender.class);

	private final String queueUrl;

	private final AmazonSQSClient sqs;

	private final Metrics metrics = new Metrics();

	public SimpleSqsSender(String queueUrl) {

		this.queueUrl = queueUrl;
		this.sqs = new AmazonSQSClient(new DefaultAWSCredentialsProviderChain().getCredentials());
	}

	/**
	 * Sends messages in a loop delaying 1 second between each message.
	 */
	public void run() {

		logger.info("Starting SimpleSQSSender");
		int batchSize = 10;
		while (true) {
			sqs.sendMessageBatch(new SendMessageBatchRequest()
					.withQueueUrl(queueUrl)
					.withEntries(IntStream.range(0, batchSize)
							.mapToObj((i) -> new SendMessageBatchRequestEntry()
									.withId(Integer.toString(i))
									.withMessageBody("{\"counter\":" + i + "}"))
							.collect(Collectors.toList())));
			metrics.sent(batchSize);
		}
	}

}
