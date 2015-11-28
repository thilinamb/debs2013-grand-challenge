package cs555.project;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * @author Thilina Buddhika
 */
public class HubBolt extends BaseBasicBolt {

    int counter = 0;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        // just print everything
        counter++;
        if(counter % 1000 == 0){
            System.out.println("Record count : " + counter);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        // no output currently
    }
}
