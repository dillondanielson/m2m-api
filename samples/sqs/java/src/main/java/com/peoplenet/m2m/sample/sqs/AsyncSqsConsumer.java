package com.peoplenet.m2m.sample.sqs;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.peoplenet.m2m.sample.sqs.metrics.Metrics;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Asynchronous SQS consumer using the AmazonSQSAsyncClient.
 */
public class AsyncSqsConsumer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(AsyncSqsConsumer.class);

	private final String queueUrl;

	private final AmazonSQSAsyncClient sqs;

	private final Metrics metrics = new Metrics();

	private final MessageHandler messageHandler = new MessageHandler();

	public AsyncSqsConsumer(String queueUrl) {

		this.queueUrl = queueUrl;
		this.sqs = new AmazonSQSAsyncClient(new DefaultAWSCredentialsProviderChain().getCredentials());
	}

	public void run() {

		logger.info("Starting AsyncSQSConsumer");
		while (true) {
			try {
				consumeNextBatch();
			} catch (QueueDoesNotExistException e) {
				logger.error("Queue does not exist for queueUrl:" + queueUrl, e);
				throw e;
			} catch (Throwable t) {
				logger.error("Error attempting to consume from queueUrl:" + queueUrl, t);
				logger.info("Waiting 5 seconds before retry.");
				sleep(5000);
			}
		}
	}

	private void consumeNextBatch() throws Throwable {

		// Receive messages, note by default PeopleNet queues are setup to long poll for up to 20 seconds.
		// This call will block until messages are available or the long polling interval expires.
		try {
			// call receive message and get() to block until results are available for the message handler
			sqs.receiveMessageAsync(new ReceiveMessageRequest()
					.withQueueUrl(queueUrl)
					.withMaxNumberOfMessages(10), messageHandler).get();
		} catch (ExecutionException e) {
			throw e.getCause();
		}
	}

	private class MessageHandler implements AsyncHandler<ReceiveMessageRequest, ReceiveMessageResult> {

		@Override public void onSuccess(ReceiveMessageRequest request, ReceiveMessageResult receiveMessageResult) {

			List<Message> messages = receiveMessageResult.getMessages();

			// process messages
			messages.stream().forEach(message -> {
				// add processing logic here
				metrics.received();
			});

			// delete messages
			sqs.deleteMessageBatchAsync(
					new DeleteMessageBatchRequest()
							.withQueueUrl(queueUrl)
							.withEntries(IntStream.range(0, messages.size())
									.mapToObj(i -> new DeleteMessageBatchRequestEntry()
											.withReceiptHandle(messages.get(i).getReceiptHandle())
											.withId(Integer.toString(i)))
									.collect(Collectors.toList())));
		}

		@Override public void onError(Exception e) {
			logger.error("Exception in async handler", e);
		}
	}

	private void sleep(long millis) {

		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			logger.error("Exception in sleep.", e);
		}
	}

}
