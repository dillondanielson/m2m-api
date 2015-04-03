package com.peoplenet.m2m.sample.amqp.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AMQPPublisher {
    private static final Logger log = Logger.getLogger(AMQPPublisher.class.getName());

    private AMQPSettings amqpSettings;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;


    public static void main(String[] args) {

        AMQPPublisher publisher = new AMQPPublisher();
        try {
            publisher.init();
            publisher.publish();
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Exception attempting to publish to AMQP topic.", t);
        } finally {
            publisher.shutdown();
        }
    }

    private void init() throws IOException {
        log.info("Initializing AMQP publisher.");
        amqpSettings = new AMQPSettings(getAMQPProperties());
        factory = configureConnectionFactory();
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(amqpSettings.getExchange(), "topic");
        log.info("AMQP connection established.");
    }


    private ConnectionFactory configureConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(amqpSettings.getUsername());
        factory.setPassword(amqpSettings.getPassword());
        factory.setVirtualHost(amqpSettings.getVirtualhost());
        factory.setHost(amqpSettings.getBrokerHost());
        factory.setPort(amqpSettings.getPort());
        factory.setAutomaticRecoveryEnabled(true);

        return factory;
    }

    /**
     * Create the AMQP connection and publish a series of messages containing the current time.
     */
    private void publish() throws IOException {

        try {
            // publish 5 messages with a 1 second delay between each.
            for (int i = 0; i < 5; i++) {
                String msg = new Date().toString();
                channel.basicPublish(amqpSettings.getExchange(), amqpSettings.getRoutingKey(), null, new Date().toString().getBytes());
                log.info("Successfully published [" + msg + "] to [" + amqpSettings.getExchange() + "] with routing key [" + amqpSettings.getRoutingKey() + "]");
                sleep(1000);
            }
            log.info("Publishing complete.");
        } finally {
            channel.close();
            connection.close();
        }
    }

    private void shutdown() {

        log.info("Publisher shutting down...");
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                // ignore
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                // ignore
            }
        }
        log.info("Publisher shutdown complete.");
    }

    private Properties getAMQPProperties() throws IOException {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("amqp.properties"));
        return props;
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            // ignore
        }
    }
}
