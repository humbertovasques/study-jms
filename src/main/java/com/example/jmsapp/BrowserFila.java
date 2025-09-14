package com.example.jmsapp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import java.util.Enumeration;

public class BrowserFila {
    public static void main(String[] args) throws NamingException, JMSException {
        ConnectionFactory factory = ServiceLocator.lookup("ConnectionFactory", ConnectionFactory.class);
        Destination destination = ServiceLocator.lookup("financeiro", Destination.class);
        Connection connection = null;
        Session session = null;

        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            QueueBrowser queueBrowser = session.createBrowser((Queue) destination);
            Enumeration enumeration = queueBrowser.getEnumeration();

            while (enumeration.hasMoreElements()) {
                TextMessage textMessage = (TextMessage) enumeration.nextElement();
                System.out.println(textMessage.getText());
            }
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
