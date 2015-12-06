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
    public static final String BALL_POSSESSION = "ball-possession";
    public static final String SHOTS_ON_GOAL = "shots-on-goal";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://somerset:61616");
            Connection connection = factory.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Setup MessageListeners for topics
            Topic playerPerfTopicDestination = session.createTopic(RUNNING_PERF);
            TopicSubscriber playerPerfSubscriber = ((ActiveMQSession)session).createSubscriber(playerPerfTopicDestination);
            playerPerfSubscriber.setMessageListener(new PlayerPerfConsumer());

            Topic ballPossessionTopicDest = session.createTopic(BALL_POSSESSION);
            TopicSubscriber ballPossessionSubscriber = ((ActiveMQSession)session).createSubscriber(ballPossessionTopicDest);
            ballPossessionSubscriber.setMessageListener(new BallPossessionConsumer());

            Topic shotsOnGoalDest = session.createTopic(SHOTS_ON_GOAL);
            TopicSubscriber shotsOnGoalSubscriber = ((ActiveMQSession)session).createSubscriber(shotsOnGoalDest);
            shotsOnGoalSubscriber.setMessageListener(new ShotsOnGoalConsumer());

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
