package cs555.project;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cs555.project.util.Constants;

/**
 * @author Thilina Buddhika
 */
public class HubBolt extends BaseBasicBolt {

    int counter = 0;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        // just print everything
        counter++;
        if (counter % 1000 == 0) {
            System.out.println("Record count : " + counter);
        }
        String playerName = tuple.getStringByField(Constants.Fields.META_NAME);
        // only emit players' data into the player-positions stream
        if (!playerName.equals("Ball") && !playerName.equals("Referee")) {
            String leg = tuple.getStringByField(Constants.Fields.META_LEG);
            if (leg.equals("L")) {  // we just consider the readings from the sensor attached to left boot
                basicOutputCollector.emit(Constants.Streams.PLAYER_POSITIONS, new Values(
                        tuple.getLongByField(Constants.Fields.RAW_TIMESTAMP),
                        tuple.getStringByField(Constants.Fields.META_NAME),
                        tuple.getStringByField(Constants.Fields.META_TEAM),
                        tuple.getDoubleByField(Constants.Fields.RAW_LOC_X),
                        tuple.getDoubleByField(Constants.Fields.RAW_LOC_Y)));
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream(Constants.Streams.PLAYER_POSITIONS, new Fields(Constants.Fields.RAW_TIMESTAMP,
                Constants.Fields.META_NAME, Constants.Fields.META_TEAM, Constants.Fields.RAW_LOC_X, Constants.Fields.RAW_LOC_Y));
    }
}
