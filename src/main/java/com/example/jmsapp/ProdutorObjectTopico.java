package com.example.jmsapp;

import com.example.jmsapp.modelo.Pedido;
import com.example.jmsapp.modelo.PedidoFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Scanner;

public class ProdutorObjectTopico {
    public static void main(String[] args) throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty("java.naming.provider.url", "tcp://192.168.0.2:61616");
        properties.setProperty("topic.loja", "topico.loja");
        InitialContext ic = new InitialContext(properties);
        ConnectionFactory cf = (ConnectionFactory) ic.lookup("ConnectionFactory");
        Connection conn = cf.createConnection();
        conn.start();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // destino
        Topic topic = (Topic) ic.lookup("loja");

        // produtor
        MessageProducer publisher = session.createProducer(topic);

        // criar menssagem
        PedidoFactory pedidoFactory = new PedidoFactory();
        Pedido pedido = pedidoFactory.geraPedidoComValores();
        StringWriter writer = new StringWriter();
        String xml;
        JAXB.marshal(pedido,writer);
        xml = writer.toString();
        ObjectMessage message = session.createObjectMessage(pedido);

        // enviar mensagem
        try {
            publisher.send(message);
            System.out.println("Menssagem enviada: \n" + message.getObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Scanner(System.in).nextLine();

        session.close();
        conn.close();
        ic.close();
    }
}
