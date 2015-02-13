package com.peoplenet.m2m.sample.subscriber;

import java.util.Properties;
import net.sf.xenqtt.message.QoS;

/**
 * Subscription settings loaded via the mqtt.properties file.
 */
public class MqttSettings {

	private String brokerUri;
	private String clientId;
	private String username;
	private String password;
	private boolean cleanSession;

	private String topic;
	private QoS qos;

	private int messageHandlerThreadPoolSize;

	MqttSettings(Properties props) {

		brokerUri = props.getProperty("brokerUri");
		clientId = props.getProperty("clientId");
		username = props.getProperty("username");
		password = props.getProperty("password");
		cleanSession = Boolean.valueOf(props.getProperty("cleanSession"));

		topic = props.getProperty("topic");
		qos = QoS.valueOf(props.getProperty("qos"));

		messageHandlerThreadPoolSize = Integer.valueOf(props.getProperty("messageHandlerThreadPoolSize"));
	}

	public String getBrokerUri() {
		return brokerUri;
	}

	public void setBrokerUri(String brokerUri) {
		this.brokerUri = brokerUri;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public void setCleanSession(boolean cleanSession) {
		this.cleanSession = cleanSession;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public QoS getQos() {
		return qos;
	}

	public void setQos(QoS qos) {
		this.qos = qos;
	}

	public int getMessageHandlerThreadPoolSize() {
		return messageHandlerThreadPoolSize;
	}

	public void setMessageHandlerThreadPoolSize(int messageHandlerThreadPoolSize) {
		this.messageHandlerThreadPoolSize = messageHandlerThreadPoolSize;
	}
}
