package cs555.project.restapi.dataaccess;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Initializes the subscriber embedded in the web-app
 * @author Thilina Buddhika
 */
@WebListener
public class SubscriptionInitializer implements ServletContextListener {

    public static final String RUNNING_PERF = "running-perf";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://somerset:61616");
            Connection connection = factory.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Setup main topic MessageListener
            Topic destination = session.createTopic(RUNNING_PERF);
            TopicSubscriber consumer = ((ActiveMQSession)session).createSubscriber(destination);
            consumer.setMessageListener(new PlayerPerfConsumer());
            // Note: important to ensure that connection.start() if
            // MessageListeners have been registered
            connection.start();
            System.out.println("Initialization is complete!");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
