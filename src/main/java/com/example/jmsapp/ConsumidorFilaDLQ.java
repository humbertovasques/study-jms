package com.example.jmsapp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.Scanner;

public class ConsumidorFilaDLQ {
    public static void main(String[] args) throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty("java.naming.provider.url", "tcp://192.168.0.2:61616");
        properties.setProperty("queue.DLQ", "ActiveMQ.DLQ");
        InitialContext ic = new InitialContext(properties);

        ConnectionFactory factory = (ConnectionFactory) ic.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) ic.lookup("DLQ");
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        ic.close();
    }
}
