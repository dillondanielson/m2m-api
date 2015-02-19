package com.peoplenet.m2m.sample.publisher;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.MqttClientFactory;
import net.sf.xenqtt.client.MqttClientListener;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.message.ConnectReturnCode;

/**
 * Sample synchronous MQTT publisher that publishes 5 messages with a 1 second delay between each message.
 */
public class Publisher implements MqttClientListener {

	private static final Logger log = Logger.getLogger(Publisher.class.getName());

	public static void main(String[] args) {

		try {
			log.info("Establishing MQTT connection.");
			Publisher publisher = new Publisher();
			publisher.publish();
		} catch (Throwable t) {
			log.log(Level.SEVERE, "Exception attempting to publish to MQTT topic.", t);
		}
	}

	/**
	 * Create the MQTT client and publish a series of messages containing the current time.
	 */
	private void publish() throws IOException {

		MqttSettings mqttSettings = new MqttSettings(getMqttProperties());
		MqttClientFactory factory = new MqttClientFactory(mqttSettings.getBrokerUri(), 1, true);

		// create new mqtt client providing this as the MqttClientListener implementation.
		// even though we're not listening for messages, a message listner must be provided to the xenqtt api.
		MqttClient client = factory.newSynchronousClient(this);

		// username and password are not required per the MQTT spec, only specify if available
		ConnectReturnCode returnCode = mqttSettings.getUsername() != null ?
				client.connect(mqttSettings.getClientId(), mqttSettings.isCleanSession(), mqttSettings.getUsername(), mqttSettings.getPassword()) :
				client.connect(mqttSettings.getClientId(), mqttSettings.isCleanSession());

		if (returnCode != ConnectReturnCode.ACCEPTED) {
			throw new IOException("Unable to connect to MQTT broker. Reason:" + returnCode);
		}
		log.info("MQTT connection established.");

		// publish 5 messages with a 1 second delay between each.
		for (int i = 0; i < 5; i++) {
			String msg = new Date().toString();
			client.publish(new PublishMessage(mqttSettings.getTopic(), mqttSettings.getQos(), new Date().toString()));
			log.info("Successfully published [" + msg + "] to [" + mqttSettings.getTopic() + "]");
			sleep(1000);
		}
		log.info("Publishing complete.");
	}

	private Properties getMqttProperties() throws IOException {
		Properties props = new Properties();
		props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mqtt.properties"));
		return props;
	}

	@Override public void publishReceived(MqttClient client, PublishMessage message) {
		// nothing to do here...
	}

	@Override public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
		// nothing to do here...
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			// ignore
		}
	}
}
