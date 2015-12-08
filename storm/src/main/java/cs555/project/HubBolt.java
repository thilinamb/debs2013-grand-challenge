package cs555.project;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cs555.project.util.Constants;
import cs555.project.util.StatCollector;

/**
 * @author Thilina Buddhika
 */
public class HubBolt extends BaseBasicBolt {

    int counter = 0;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        // just print everything
        if (counter % 1000 == 0) {
            StatCollector.getInstance().record(counter);
        }
        counter++;

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
        // emit ball possession data
        if (!playerName.equals("Referee")) {
            basicOutputCollector.emit(Constants.Streams.PLAYER_BALL_POSITIONS, new Values(
                    tuple.getLongByField(Constants.Fields.EMIT_TS),
                    tuple.getLongByField(Constants.Fields.RAW_TIMESTAMP),
                    tuple.getStringByField(Constants.Fields.META_NAME),
                    tuple.getStringByField(Constants.Fields.META_TEAM),
                    tuple.getStringByField(Constants.Fields.META_LEG),
                    tuple.getDoubleByField(Constants.Fields.RAW_LOC_X),
                    tuple.getDoubleByField(Constants.Fields.RAW_LOC_Y),
                    tuple.getDoubleByField(Constants.Fields.RAW_ACCELERATION),
                    tuple.getDoubleByField(Constants.Fields.RAW_VELOCITY),
                    tuple.getDoubleByField(Constants.Fields.RAW_VEL_X),
                    tuple.getDoubleByField(Constants.Fields.RAW_VEL_Y)));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        // player positions for calculate their running performance
        outputFieldsDeclarer.declareStream(Constants.Streams.PLAYER_POSITIONS, new Fields(Constants.Fields.RAW_TIMESTAMP,
                Constants.Fields.META_NAME, Constants.Fields.META_TEAM, Constants.Fields.RAW_LOC_X, Constants.Fields.RAW_LOC_Y));
        // position of players and ball to calculate the ball possession
        outputFieldsDeclarer.declareStream(Constants.Streams.PLAYER_BALL_POSITIONS, new Fields(Constants.Fields.EMIT_TS,
                Constants.Fields.RAW_TIMESTAMP, Constants.Fields.META_NAME, Constants.Fields.META_TEAM, Constants.Fields.META_LEG,
                Constants.Fields.RAW_LOC_X, Constants.Fields.RAW_LOC_Y, Constants.Fields.RAW_ACCELERATION,
                Constants.Fields.RAW_VELOCITY, Constants.Fields.RAW_VEL_X, Constants.Fields.RAW_VEL_Y));
    }
}
