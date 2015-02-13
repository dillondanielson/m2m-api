package com.peoplenet.m2m.sample.subscriber;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.MqttClientFactory;
import net.sf.xenqtt.client.MqttClientListener;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.client.Subscription;
import net.sf.xenqtt.message.ConnectReturnCode;

/**
 * Sample synchronous MQTT subscriber using the <a href="http://xenqtt.sourceforge.net/documentation.html">XenQtt</a> library.
 */
public class Subscriber implements MqttClientListener {

	private static final Logger log = Logger.getLogger(Subscriber.class.getName());
	private static Subscriber subscriber;

	public static void main(String[] args) {

		try {
			log.info("Initializing MQTT subscriber...");
			subscriber = new Subscriber();
			subscriber.subscribe();
			Thread.currentThread().join();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Exception attempting to subscribe to MQTT topic. Error message: ", e);
		}
	}

	/**
	 * Create the MQTT client, connect and subscribe.
	 */
	private void subscribe() throws IOException {

		MqttSettings mqttSettings = new MqttSettings(getMqttProperties());
		MqttClientFactory factory = new MqttClientFactory(mqttSettings.getBrokerUri(), mqttSettings.getMessageHandlerThreadPoolSize(), true);
		MqttClient client = factory.newSynchronousClient(this);

		// username and password are not required per the MQTT spec, only specify if available
		ConnectReturnCode returnCode = mqttSettings.getUsername() != null ?
				client.connect(mqttSettings.getClientId(), false, mqttSettings.getUsername(), mqttSettings.getPassword()) :
				client.connect(mqttSettings.getClientId(), false);

		if (returnCode != ConnectReturnCode.ACCEPTED) {
			throw new IOException("Unable to connect to MQTT broker. Reason:" + returnCode);
		}
		log.info("MQTT connection established.");

		Subscription subscription = new Subscription(mqttSettings.getTopic(), mqttSettings.getQos());
		client.subscribe(new Subscription[] { subscription });
		log.info("Successfully subscribed to " + subscription);
	}

	private Properties getMqttProperties() throws IOException {
		Properties props = new Properties();
		props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mqtt.properties"));
		return props;
	}

	/**
	 * Handle each message with this method.
	 */
	@Override public void publishReceived(MqttClient client, PublishMessage message) {

		log.info("Received: " + message.getPayloadString());
	}

	/**
	 * Called if the MQTT connection becomes disconnected. In most cases, XenQTT will attempt to re-establish the connection.
	 */
	@Override public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {

		log.info("MQTT connection disconnected. Reconnecting:" + reconnecting);
	}
}
