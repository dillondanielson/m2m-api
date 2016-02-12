package com.peoplenet.m2m.sample.sqs;

public class App {

	/**
	 * Consumes and logs messages from the specified queue.
	 *
	 * @param args args[0] the queue url
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Please specify the SQS queue url.");
			System.exit(1);
		}
		String queueUrl = args[0];

		// Start consumer
		SimpleSqsConsumer consumer = new SimpleSqsConsumer(queueUrl);
		new Thread(consumer).start();

		// Start the sender
		SimpleSqsSender sender = new SimpleSqsSender(queueUrl);
		new Thread(sender).start();
	}

}
