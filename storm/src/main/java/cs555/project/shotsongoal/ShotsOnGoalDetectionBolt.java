package cs555.project.shotsongoal;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cs555.project.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Thilina Buddhika
 */
public class ShotsOnGoalDetectionBolt extends BaseBasicBolt {

    private int teamAShotsOnGoal;
    private int teamBShotsOnGoal;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        double x = tuple.getDoubleByField(Constants.Fields.RAW_LOC_X);
        double y = tuple.getDoubleByField(Constants.Fields.RAW_LOC_Y);
        double velocity = tuple.getDoubleByField(Constants.Fields.RAW_VELOCITY);
        double vx = tuple.getDoubleByField(Constants.Fields.RAW_VEL_X);
        double vy = tuple.getDoubleByField(Constants.Fields.RAW_VEL_Y);
        double projectedVx = velocity * vx * Math.pow(10, 10);
        double projectedVy = velocity * vy * Math.pow(10, 10);
        double projectedX = x + projectedVx * 1.5 * 1000;
        double projectedY = y + projectedVy * 1.5 * 1000;
        boolean shotsOnGoal = onGoal(tuple.getStringByField(Constants.Fields.META_TEAM), projectedX, projectedY);
        if(shotsOnGoal){
            System.out.println("Shot on goal detected by team " + tuple.getStringByField(Constants.Fields.META_TEAM));
            try {
                basicOutputCollector.emit(Constants.Streams.SHOTS_ON_GOAL_TO_PUBLISHER,
                        new Values(Constants.Topics.SHOTS_ON_GOAL, getBinaryPayload(teamAShotsOnGoal, teamBShotsOnGoal)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream(Constants.Streams.SHOTS_ON_GOAL_TO_PUBLISHER,
                new Fields(Constants.Fields.TOPIC, Constants.Fields.PAYLOAD));
    }

    public boolean onGoal(String team, double projectedX, double projectedY) {
        boolean shotsOnGoal = false;
        if (team.equals("B")) {
            // team 1: y > 22578.5 and y < 29898.5, x = 33941.0, z < 2440.0
            if ((projectedX >= 22578.5 && projectedX <= 29898.5) && (projectedY >= 33941)) {
                shotsOnGoal = true;
                teamBShotsOnGoal++;
                System.out.println("It's a shot on goal by team B!");
            }
        } else {
            // team 2: y > 22560.0 and y < 29880.0, x = -33968.0, z < 2440.0
            if ((projectedX >= 22560 && projectedX <= 29880) && (projectedY >= -33968)) {
                shotsOnGoal = true;
                teamAShotsOnGoal++;
                System.out.println("It's a shot on goal by team A!");
            }
        }
        return shotsOnGoal;
    }

    private byte[] getBinaryPayload(int teamAShotsOnGoal, int teamBShotsOnGoal) throws IOException {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new java.io.DataOutputStream(baos);
            dos.writeInt(teamAShotsOnGoal);
            dos.writeInt(teamBShotsOnGoal);
            dos.flush();
            return baos.toByteArray();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
