package com.example.jmsapp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLocator {
    private static Context context;
    private static final Map<String, Object> cache = new ConcurrentHashMap<>();

    static {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.put(Context.PROVIDER_URL, "tcp://192.168.0.2:61616");
            env.put("queue.financeiro", "fila.financeiro");
            context = new InitialContext(env);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(String name, Class<T> type) {
        try {
            if (cache.containsKey(name)) {
                return (T) cache.get(name);
            }

            Object obj = context.lookup(name);
            cache.put(name, obj);
            return (T) obj;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
