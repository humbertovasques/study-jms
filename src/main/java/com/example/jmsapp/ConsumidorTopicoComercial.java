package com.example.jmsapp;

import com.example.jmsapp.modelo.Pedido;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.Scanner;

public class ConsumidorTopicoComercial {
    public static void main(String[] args) throws NamingException, JMSException {

        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","java.lang,com.example.jmsapp.modelo");

        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty("java.naming.provider.url", "tcp://192.168.0.2:61616");
        properties.setProperty("topic.loja", "topico.loja");
        InitialContext ic = new InitialContext(properties);

        ConnectionFactory factory = (ConnectionFactory) ic.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.setClientID("comercialID");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) ic.lookup("loja");
        MessageConsumer consumer = session.createDurableSubscriber(topic, "comercialSubscriber");

        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                try {
                    Pedido pedido = (Pedido) objectMessage.getObject();
                    System.out.println(pedido);
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
