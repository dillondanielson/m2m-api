package com.peoplenet.m2m.sample.publisher;

import java.util.Properties;
import net.sf.xenqtt.message.QoS;

/**
 * Subscription settings loaded via the mqtt.properties file.
 */
public class MqttSettings {

	private final String brokerUri;
	private final String clientId;
	private final String username;
	private final String password;
	private final boolean cleanSession;

	private final String topic;
	private final QoS qos;

	MqttSettings(Properties props) {

		brokerUri = props.getProperty("brokerUri");
		clientId = props.getProperty("clientId");
		username = props.getProperty("username");
		password = props.getProperty("password");
		cleanSession = Boolean.valueOf(props.getProperty("cleanSession"));

		topic = props.getProperty("topic");
		qos = QoS.valueOf(props.getProperty("qos"));
	}

	public String getBrokerUri() {
		return brokerUri;
	}

	public String getClientId() {
		return clientId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public String getTopic() {
		return topic;
	}

	public QoS getQos() {
		return qos;
	}

}
