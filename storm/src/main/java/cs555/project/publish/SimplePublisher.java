package cs555.project.publish;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.*;
import java.util.Random;

public class SimplePublisher {
    private static final Log LOG = LogFactory.getLog(SimplePublisher.class);

    private static final int MESSAGE_DELAY_MILLISECONDS = 100;
    private static final int NUM_MESSAGES_TO_BE_SENT = 500;

    public static void main(String args[]) {
        Connection connection = null;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://somerset:61616");
            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("testq");
            MessageProducer producer = session.createProducer(destination);
            Random random = new Random(123);
            for (int i = 1; i <= NUM_MESSAGES_TO_BE_SENT; i++) {
                BytesMessage message = session.createBytesMessage();
                message.writeUTF("Tom Sawyer");
                message.writeDouble(123.456 + random.nextInt(100));
                producer.send(message);
                Thread.sleep(MESSAGE_DELAY_MILLISECONDS);
            }
            // Cleanup
            producer.close();
            session.close();
        } catch (Throwable t) {
            LOG.error(t);
        } finally {
            // Cleanup code
            // In general, you should always close producers, consumers,
            // sessions, and connections in reverse order of creation.
            // For this simple example, a JMS connection.close will
            // clean up all other resources.
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    LOG.error(e);
                }
            }
        }
    }
}