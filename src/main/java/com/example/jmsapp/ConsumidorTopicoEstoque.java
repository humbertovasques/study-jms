package com.example.jmsapp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.Scanner;

public class ConsumidorTopicoEstoque {
    public static void main(String[] args) throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty("java.naming.provider.url", "tcp://192.168.0.2:61616");
        properties.setProperty("topic.loja", "topico.loja");
        InitialContext ic = new InitialContext(properties);

        ConnectionFactory factory = (ConnectionFactory) ic.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.setClientID("estoqueID");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) ic.lookup("loja");

        MessageConsumer consumerFisico = session.createDurableSubscriber(topic, "estoqueFisicoSubscriber", "digitalProduct is null OR digitalProduct=false", false);
        consumerFisico.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("Message text: " + textMessage.getText());
                    System.out.println("Consumer: consumerFisico");
                    System.out.println("Destination: " + textMessage.getJMSDestination());
                    System.out.println("Full message: " + textMessage);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        MessageConsumer consumerDigital = session.createDurableSubscriber(topic, "estoqueDigitalSubscriber", "digitalProduct=true", false);
        consumerDigital.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println();
                    System.out.println("Message text: " + textMessage.getText());
                    System.out.println("Consumer: consumerDigital");
                    System.out.println("Destination: " + textMessage.getJMSDestination());
                    System.out.println("Full Message: " + textMessage);
                } catch (JMSException e) {
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
