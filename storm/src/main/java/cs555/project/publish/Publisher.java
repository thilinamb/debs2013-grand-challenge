package cs555.project.publish;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Thilina Buddhika
 */
public class Publisher {

    private final String amqpAddrStr;
    private Session session;
    private Map<String, MessageProducer> producerMap = new HashMap<>();

    public Publisher(String amqpAddrStr) throws JMSException {
        this.amqpAddrStr = amqpAddrStr;
        init();
    }

    private void init() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(amqpAddrStr);
        try {
            Connection connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void publish(String topic, byte[] bytes) throws JMSException {
        try {
            MessageProducer producer = producerMap.get(topic);
            if(producer == null){
                Destination destination = session.createTopic(topic);
                producer = session.createProducer(destination);
                producerMap.put(topic, producer);
            }
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeInt(bytes.length);
            bytesMessage.writeBytes(bytes);
            producer.send(bytesMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
