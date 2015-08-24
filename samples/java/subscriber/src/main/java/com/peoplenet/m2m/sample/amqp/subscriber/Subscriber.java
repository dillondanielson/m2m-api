package com.peoplenet.m2m.sample.amqp.subscriber;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Subscriber {
    private static final Logger log = Logger.getLogger(Subscriber.class.getName());

    private AmqpSettings amqpSettings;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;


    public static void main(String[] args) {

        Subscriber subscriber = new Subscriber();
        try {
            subscriber.init();
            subscriber.subscribe();
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Exception attempting to subscribe to AMQP topic.", t);
        }
    }

    private void init() throws IOException {
        log.info("Initializing AMQP publisher.");
        amqpSettings = new AmqpSettings(getAMQPProperties());
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

        channel.queueDeclare(amqpSettings.getQueueName(), false, false, false, null);
        channel.queueBind(amqpSettings.getQueueName(), amqpSettings.getExchange(), amqpSettings.getRoutingKey());
        ConsumerHandler consumer = new ConsumerHandler(channel);
        channel.basicConsume(amqpSettings.getQueueName(), true, consumer);
    }

    private Properties getAMQPProperties() throws IOException {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("amqp.properties"));
        return props;
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
