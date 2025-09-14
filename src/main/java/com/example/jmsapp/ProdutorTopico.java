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

public class ProdutorTopico {
    public static void main(String[] args) throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty("java.naming.provider.url", "tcp://192.168.0.2:61616");
        properties.setProperty("topic.loja", "topico.loja");
        InitialContext ic = new InitialContext(properties);

        ConnectionFactory factory = (ConnectionFactory) ic.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) ic.lookup("loja");
        MessageProducer producer = session.createProducer(destination);

        Message notebook = session.createTextMessage("Produto fisico: notebook. ");
        producer.send(notebook);

        Message notebookWithProp = session.createTextMessage("Produto fisico com prop digitalProduct=false: notebook. ");
        notebookWithProp.setBooleanProperty("digitalProduct", false);
        producer.send(notebookWithProp);

        Message ebook = session.createTextMessage("Produto digital: ebook. ");
        ebook.setBooleanProperty("digitalProduct", true);
        producer.send(ebook);

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        ic.close();
    }
}
