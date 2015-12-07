package cs555.project.possession;

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
public class BallPossessionDetectionBolt extends BaseBasicBolt {

    private long startTime;
    private long lastSwitchTime;
    private long teamAPossessionTime;
    private long teamBPossessionTime;
    private String lastOwner;
    private long lastEmittedTime;
    private long totalTimeElapsed;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        if (startTime == 0) {
            startTime = tuple.getLongByField(Constants.Fields.RAW_TIMESTAMP);
            lastSwitchTime = startTime;
            lastOwner = tuple.getStringByField(Constants.Fields.META_TEAM);
        } else {
            String currentOwner = tuple.getStringByField(Constants.Fields.META_TEAM);
            long currentSwitchTime = tuple.getLongByField(Constants.Fields.RAW_TIMESTAMP);
            // we need to consider if the other team gets hold of the ball
            long duration = currentSwitchTime - lastSwitchTime;
            if (lastOwner.equals("A")) {
                teamAPossessionTime += duration;
            } else {
                teamBPossessionTime += duration;
            }
            lastSwitchTime = currentSwitchTime;
            lastOwner = currentOwner;
            totalTimeElapsed = currentSwitchTime - startTime;
        }
        int c = 0;
        if (System.currentTimeMillis() - lastEmittedTime > 1000) {
            try {
                byte[] binaryPayload = prepareBinaryMessage((teamAPossessionTime * 1.0) / totalTimeElapsed,
                        (teamBPossessionTime * 1.0) / totalTimeElapsed);
                lastEmittedTime = System.currentTimeMillis();
                basicOutputCollector.emit(Constants.Streams.BALL_POSSESSION_TO_PUBLISHER,
                        new Values(Constants.Topics.BALL_POSSESSION, binaryPayload));
            } catch (IOException e) {
                e.printStackTrace();
            }
            c++;
        }
        if (c == 5) {
            System.out.println("A: " + (teamAPossessionTime * 1.0) / totalTimeElapsed + ", B: " +
                    (teamBPossessionTime * 1.0) / totalTimeElapsed);
            c = 0;
        }
    }

    private byte[] prepareBinaryMessage(double teamAPossesion, double teamBPossession) throws IOException {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new java.io.DataOutputStream(baos);
            dos.writeDouble(teamAPossesion);
            dos.writeDouble(teamBPossession);
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

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream(Constants.Streams.BALL_POSSESSION_TO_PUBLISHER,
                new Fields(Constants.Fields.TOPIC, Constants.Fields.PAYLOAD));
    }
}
