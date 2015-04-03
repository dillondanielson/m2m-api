package com.peoplenet.m2m.sample.amqp.subscriber;

import java.util.Properties;

/**
 * Created by agupta on 4/1/2015.
 */
public class AMQPSettings {

    private final String brokerHost;
    private final Integer port;
    private final String username;
    private final String password;
    private final String virtualhost;
    private final String exchange;
    private final String queueName;
    private final String routingKey;
    private final Integer prefetchCount;
    private final long waitTimeMillis;

    public AMQPSettings(Properties props) {
        brokerHost = props.getProperty("hostname");
        virtualhost = props.getProperty("virtualhost");
        port = Integer.valueOf(props.getProperty("port"));
        username = props.getProperty("username");
        password = props.getProperty("password");
        routingKey = props.getProperty("routingKey");
        exchange = props.getProperty("exchange");
        queueName = props.getProperty("queueName");
        prefetchCount = Integer.valueOf(props.getProperty("prefetchCount"));
        waitTimeMillis = Long.valueOf(props.getProperty("waitTimeMillis"));
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public long getWaitTimeMillis() {
        return waitTimeMillis;
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

    public String getQueueName() {
        return queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public Integer getPrefetchCount() {
        return prefetchCount;
    }
}
