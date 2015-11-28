package cs555.project;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cs555.project.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Enrich raw data with meta-data by adding player's name, leg and team.
 * Similarly identify the sensors attached to ball and referee
 * @author Thilina Buddhika
 */
public class EnrichBolt extends BaseRichBolt {

    private class SensorMetadata {
        private String playerName;
        private String team = ""; // default values
        private String leg = "X"; // default values

        public SensorMetadata(String playerName, String team, String leg) {
            this.playerName = playerName;
            this.team = team;
            this.leg = leg;
        }

        public SensorMetadata(String playerName) {
            this.playerName = playerName;
        }

        public SensorMetadata(String playerName, String leg) {
            this.playerName = playerName;
            this.leg = leg;
        }
    }

    private Map<Integer, SensorMetadata> metadata = new HashMap<>();
    private OutputCollector outputCollector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
        // ball
        metadata.put(4, new SensorMetadata("Ball"));
        metadata.put(8, new SensorMetadata("Ball"));
        metadata.put(10, new SensorMetadata("Ball"));
        metadata.put(12, new SensorMetadata("Ball"));
        // referee
        metadata.put(105, new SensorMetadata("Referee","L"));
        metadata.put(106, new SensorMetadata("Referee","R"));
        // team A
        metadata.put(13, new SensorMetadata("Nick Gertje","A","L"));
        metadata.put(14, new SensorMetadata("Nick Gertje","A","R"));
        metadata.put(47, new SensorMetadata("Dennis Dotterweich","A","L"));
        metadata.put(16, new SensorMetadata("Dennis Dotterweich","A","R"));
        metadata.put(49, new SensorMetadata("Niklas Waelzlein","A","L"));
        metadata.put(88, new SensorMetadata("Niklas Waelzlein","A","R"));
        metadata.put(19, new SensorMetadata("Wili Sommer","A","L"));
        metadata.put(52, new SensorMetadata("Wili Sommer","A","R"));
        metadata.put(53, new SensorMetadata("Philipp Harlass","A","L"));
        metadata.put(54, new SensorMetadata("Philipp Harlass","A","R"));
        metadata.put(23, new SensorMetadata("Roman Hartleb","A","L"));
        metadata.put(24, new SensorMetadata("Roman Hartleb","A","R"));
        metadata.put(57, new SensorMetadata("Erik Engelhardt","A","L"));
        metadata.put(58, new SensorMetadata("Erik Engelhardt","A","R"));
        metadata.put(59, new SensorMetadata("Sandro Schneider","A","L"));
        metadata.put(60, new SensorMetadata("Sandro Schneider","A","R"));
        // team B
        metadata.put(61, new SensorMetadata("Leon Krapf","B","L"));
        metadata.put(62, new SensorMetadata("Leon Krapf","B","R"));
        metadata.put(63, new SensorMetadata("Kevin Baer","B","L"));
        metadata.put(64, new SensorMetadata("Kevin Baer","B","R"));
        metadata.put(65, new SensorMetadata("Luca Ziegler","B","L"));
        metadata.put(66, new SensorMetadata("Luca Ziegler","B","R"));
        metadata.put(67, new SensorMetadata("Ben Mueller","B","L"));
        metadata.put(68, new SensorMetadata("Ben Mueller","B","R"));
        metadata.put(69, new SensorMetadata("Vale Reitstetter","B","L"));
        metadata.put(38, new SensorMetadata("Vale Reitstetter","B","R"));
        metadata.put(71, new SensorMetadata("Christopher Lee","B","L"));
        metadata.put(40, new SensorMetadata("Christopher Lee","B","R"));
        metadata.put(73, new SensorMetadata("Leon Heinze","B","L"));
        metadata.put(74, new SensorMetadata("Leon Heinze","B","R"));
        metadata.put(75, new SensorMetadata("Leo Langhans","B","L"));
        metadata.put(44, new SensorMetadata("Leo Langhans","B","R"));
    }

    @Override
    public void execute(Tuple tuple) {
        int sensorId = tuple.getInteger(0);
        SensorMetadata meta = metadata.get(sensorId);
        if(meta != null) {
            outputCollector.emit(new Values(sensorId, tuple.getLong(1),
                    meta.playerName, meta.team, meta.leg,
                    tuple.getDouble(2), tuple.getDouble(3), tuple.getDouble(4),
                    tuple.getDouble(5), tuple.getDouble(6),
                    tuple.getDouble(7), tuple.getDouble(8), tuple.getDouble(9),
                    tuple.getDouble(10), tuple.getDouble(11), tuple.getDouble(12)));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(Constants.Fields.RAW_SID, Constants.Fields.RAW_TIMESTAMP,
                Constants.Fields.META_NAME, Constants.Fields.META_TEAM, Constants.Fields.META_LEG,
                Constants.Fields.RAW_LOC_X, Constants.Fields.RAW_LOC_Y, Constants.Fields.RAW_LOC_Z,
                Constants.Fields.RAW_VELOCITY, Constants.Fields.RAW_ACCELERATION,
                Constants.Fields.RAW_VEL_X, Constants.Fields.RAW_VEL_Y, Constants.Fields.RAW_VEL_Z,
                Constants.Fields.RAW_ACC_X, Constants.Fields.RAW_ACC_Y, Constants.Fields.RAW_ACC_Z));
    }
}
