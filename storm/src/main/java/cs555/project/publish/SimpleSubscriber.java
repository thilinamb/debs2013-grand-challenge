package cs555.project.publish;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

public class SimpleSubscriber implements MessageListener{
    private static final Log LOG = LogFactory.getLog(SimpleSubscriber.class);
    private static CountDownLatch waitTillAllMessages = new CountDownLatch(1);

    private int count;

    public static void main(String args[]) {
        Connection connection = null;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://somerset:61616");
            connection = factory.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic("testq");


            // Setup main topic MessageListener
            TopicSubscriber consumer = ((ActiveMQSession)session).createSubscriber(destination);
            consumer.setMessageListener(new SimpleSubscriber());

            // Note: important to ensure that connection.start() if
            // MessageListeners have been registered
            connection.start();
            waitTillAllMessages.await();
            System.out.println("Received all 500 messages!");
            consumer.close();
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

    @Override
    public void onMessage(Message message) {
        if (message instanceof BytesMessage){
            BytesMessage bytesMessage =(BytesMessage)message;
            try {
                System.out.println(bytesMessage.readUTF() + ":" + bytesMessage.readDouble());
                count++;
                if(count == 500){
                    waitTillAllMessages.countDown();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}