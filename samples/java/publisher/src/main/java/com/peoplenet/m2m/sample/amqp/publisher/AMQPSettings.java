package com.peoplenet.m2m.sample.amqp.publisher;

import net.sf.xenqtt.message.QoS;

import java.util.Properties;

public class AMQPSettings {

    private final String brokerHost;
    private final Integer port;
    private final String username;
    private final String password;
    private final String virtualhost;
    private final String exchange;
    private final String routingKey;


    public AMQPSettings(Properties props) {
        brokerHost = props.getProperty("hostname");
        virtualhost = props.getProperty("virtualhost");
        port = Integer.valueOf(props.getProperty("port"));
        username = props.getProperty("username");
        password = props.getProperty("password");
        routingKey = props.getProperty("routingKey");
        exchange = props.getProperty("exchange");
   }

    public String getBrokerHost() {
        return brokerHost;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExchange() {
        return exchange;
    }

    public String getVirtualhost() {
        return virtualhost;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
