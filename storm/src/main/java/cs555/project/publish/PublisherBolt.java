package cs555.project.publish;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import cs555.project.util.Constants;

import javax.jms.JMSException;
import java.util.Map;

/**
 * Publishes data to the pub/sub system
 * @author Thilina Buddhika
 */
public class PublisherBolt extends BaseRichBolt {

    private Publisher publisher;
    private OutputCollector outputCollector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
        String amqpAddrStr = (String) map.get(Constants.AMQP_ADDR);
        try {
            publisher = new Publisher(amqpAddrStr);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Tuple tuple) {
        String topic = tuple.getStringByField(Constants.Fields.TOPIC);
        byte[] message = tuple.getBinaryByField(Constants.Fields.PAYLOAD);
        /*try {
            publisher.publish(topic, message);
        } catch (JMSException e) {
            e.printStackTrace();
        }*/
        outputCollector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        // none
    }
}
