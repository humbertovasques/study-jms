package com.example.jmsapp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.Scanner;

public class ProdutorFila {
    public static void main(String[] args) throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty("java.naming.provider.url", "tcp://192.168.0.2:61616");
        properties.setProperty("queue.financeiro", "fila.financeiro");
        InitialContext ic = new InitialContext(properties);

        ConnectionFactory factory = (ConnectionFactory) ic.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) ic.lookup("financeiro");
        MessageProducer producer = session.createProducer(destination);

        for (int i = 0; i < 100; i++) {
            Message textMessage = session.createTextMessage("Mensagem " + i);
            producer.send(textMessage);
        }

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        ic.close();
    }
}
