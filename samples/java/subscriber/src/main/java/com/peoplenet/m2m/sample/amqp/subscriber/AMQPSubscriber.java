package com.peoplenet.m2m.sample.amqp.subscriber;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AMQPSubscriber {
    private static final Logger log = Logger.getLogger(AMQPSubscriber.class.getName());

    private AMQPSettings amqpSettings;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;


    public static void main(String[] args) {

        AMQPSubscriber subscriber = new AMQPSubscriber();
        try {
            subscriber.init();
            subscriber.subscribe();
            //subscriber.waitForDelay();
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Exception attempting to subscribe to AMQP topic.", t);
        } finally {
           // subscriber.shutdown();
        }
    }

    private void waitForDelay() throws InterruptedException {
        // sleep for the number of millis specified in mqtt.properties
        if (amqpSettings.getWaitTimeMillis() == -1) {
            log.info("Waiting for messages until program termination.");
            Thread.sleep(Long.MAX_VALUE);
        } else {
            log.info("Waiting " + amqpSettings.getWaitTimeMillis() + " ms for incoming messages.");
            Thread.sleep(amqpSettings.getWaitTimeMillis());
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
        factory.setHost(amqpSettings.getBrokerHost());
        factory.setUsername(amqpSettings.getUsername());
        factory.setPassword(amqpSettings.getPassword());
        factory.setVirtualHost(amqpSettings.getVirtualhost());
        factory.setPort(amqpSettings.getPort());
        factory.setAutomaticRecoveryEnabled(true);

        return factory;
    }

    /**
     * Create the AMQP connection and publish a series of messages containing the current time.
     */
    private void subscribe() throws Exception {

        try {
            channel.queueDeclare(amqpSettings.getQueueName(), false, false, false, null);
            channel.queueBind(amqpSettings.getQueueName(), amqpSettings.getExchange(), amqpSettings.getRoutingKey());
            ConsumerHandler consumer = new ConsumerHandler(channel);
            channel.basicConsume(amqpSettings.getQueueName(), true, consumer);
        } finally {
          //  channel.close();
           // connection.close();
        }
    }

    private void shutdown() {

        log.info("Subscriber shutting down...");
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
        log.info("Subscriber shutdown complete.");
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

    static class ConsumerHandler extends DefaultConsumer {

        /**
         * Constructs a new instance and records its association to the passed-in channel.
         *
         * @param channel the channel to which this consumer is attached
         */
        public ConsumerHandler(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag,
                                   Envelope envelope,
                                   AMQP.BasicProperties properties,
                                   byte[] body) throws IOException {
            log.info("Message received");
            String message = new String(body);
            String routingKey = envelope.getRoutingKey();
            log.info("Received '" + routingKey + "':'" + message + "'");
        }

    }
}
