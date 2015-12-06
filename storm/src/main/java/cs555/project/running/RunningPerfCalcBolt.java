package cs555.project.running;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cs555.project.util.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Thilina Buddhika
 */
public class RunningPerfCalcBolt extends BaseBasicBolt {

    private Map<String, SlidingWindow> playerRunningPerfWindows = new HashMap<>();
    private Map<String, Double> playerRunningDistanceOverWindow = new HashMap<>();
    private double lastEmittedAt = 0;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        final String playerName = tuple.getStringByField(Constants.Fields.META_NAME);
        double x = tuple.getDoubleByField(Constants.Fields.RAW_LOC_X);
        double y = tuple.getDoubleByField(Constants.Fields.RAW_LOC_Y);
        long ts = tuple.getLongByField(Constants.Fields.RAW_TIMESTAMP);

        SlidingWindow playerWindow;
        DeltaDistanceSWEntry lastEntry;
        double distance = 0;
        double playerRunningDistance = 0;
        if (playerRunningPerfWindows.containsKey(playerName)) {
            playerWindow = playerRunningPerfWindows.get(playerName);
            lastEntry = (DeltaDistanceSWEntry) playerWindow.getLastEntry();
            distance = Util.calculateDistance(lastEntry.getX(), x, lastEntry.getY(), y);
            playerRunningDistance = playerRunningDistanceOverWindow.get(playerName);
            playerRunningDistance += distance;
        } else {
            playerWindow = new SlidingWindow(Constants.PLAYER_PERF_SLIDING_WINDOW_LEN);
            playerRunningPerfWindows.put(playerName, playerWindow);
        }
        playerRunningDistanceOverWindow.put(playerName, playerRunningDistance);
        DeltaDistanceSWEntry newEntry = new DeltaDistanceSWEntry(ts, distance, x, y);
        playerWindow.add(newEntry, new SlidingWindowCallback() {
            @Override
            public void remove(List<SlidingWindowEntry> entries) {
                double totalReduced = 0;
                for (SlidingWindowEntry entry : entries) {
                    DeltaDistanceSWEntry distanceSWEntry = (DeltaDistanceSWEntry) entry;
                    totalReduced += distanceSWEntry.getDistance();
                }
                playerRunningDistanceOverWindow.put(playerName, playerRunningDistanceOverWindow.get(playerName) -
                        totalReduced);
            }
        });
        // calculate the avg speed over the sliding window in km/h
        double speed = (playerRunningDistanceOverWindow.get(playerName) * 60 * 60) /
                (Constants.PLAYER_PERF_SLIDING_WINDOW_LEN * 1000);
        speed = Math.round(speed * 100);
        speed = speed/100;
        String team = tuple.getStringByField(Constants.Fields.META_TEAM);
        // stream rate should be 50 Hz
        long now = System.currentTimeMillis();
        if ((now - lastEmittedAt) >= 20) {
            try {
                basicOutputCollector.emit(Constants.Streams.PLAYER_PERF_TO_PUBLISHER,
                        new Values(Constants.Topics.RUNNING_PERF, createBinaryPayload(playerName, team, speed)));
                lastEmittedAt = now;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] createBinaryPayload(String playerName, String team, double speed) throws IOException {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new java.io.DataOutputStream(baos);
            dos.writeUTF(playerName);
            dos.writeUTF(team);
            dos.writeDouble(speed);
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
        outputFieldsDeclarer.declareStream(Constants.Streams.PLAYER_PERF_TO_PUBLISHER,
                new Fields(Constants.Fields.TOPIC, Constants.Fields.PAYLOAD));
    }

}
