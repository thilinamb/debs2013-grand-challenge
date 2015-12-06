package cs555.project.possession;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cs555.project.util.Constants;
import cs555.project.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Thilina Buddhika
 */
public class BallHitDetectionBolt extends BaseBasicBolt {

    private class PlayerPosition {
        double lx;
        double ly;
        double rx;
        double ry;
        String team;
        double distanceToBall;

        PlayerPosition(String team) {
            this.team = team;
        }

        void updatePosition(String leg, double x, double y) {
            if (leg.equals("L")) {
                lx = x;
                ly = y;
            } else {
                rx = x;
                ry = y;
            }
        }
    }

    private Map<String, PlayerPosition> playerLocations = new HashMap<>();
    private PlayerPosition currentBallHolder;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String playerName = tuple.getStringByField(Constants.Fields.META_NAME);
        double x = tuple.getDoubleByField(Constants.Fields.RAW_LOC_X);
        double y = tuple.getDoubleByField(Constants.Fields.RAW_LOC_Y);
        if (!playerName.equals("Ball")) {
            PlayerPosition playerPosition;
            String leg = tuple.getStringByField(Constants.Fields.META_LEG);
            if (playerLocations.containsKey(playerName)) {
                playerPosition = playerLocations.get(playerName);
            } else {
                String team = tuple.getStringByField(Constants.Fields.META_TEAM);
                playerPosition = new PlayerPosition(team);
                playerLocations.put(playerName, playerPosition);
            }
            playerPosition.updatePosition(leg, x, y);
        } else {
            double acceleration = tuple.getDoubleByField(Constants.Fields.RAW_ACCELERATION) / Math.pow(10, 6);
            PlayerPosition closestPlayer = findClosestPlayer(x, y);
            if (currentBallHolder == null) {
                currentBallHolder = closestPlayer;
            } else {
                currentBallHolder = closestPlayer;
                if (currentBallHolder.distanceToBall <= 1000 && acceleration >= 55) {
                    // TODO: update it to emit to a stream that calculates the ball posession
                    // previous ball holder has hit the ball
                    System.out.println("[EVENT] Ball belongs to team " + currentBallHolder.team);
                    // emit to a stream to calculate shots on goal
                    basicOutputCollector.emit(Constants.Streams.SHOTS_ON_GALL, new Values(tuple.getLongByField(
                            Constants.Fields.RAW_TIMESTAMP), currentBallHolder.team, x, y, Constants.Fields.RAW_VELOCITY,
                            Constants.Fields.RAW_VEL_X, Constants.Fields.RAW_VEL_Y));
                    basicOutputCollector.emit(Constants.Streams.BALL_POSSESSION, new Values(tuple.getLongByField(
                            Constants.Fields.RAW_TIMESTAMP), currentBallHolder.team));
                }
                System.out.println("Ball closest to " + closestPlayer.team
                        + ", distance " + closestPlayer.distanceToBall + ", acceleration: " + acceleration);
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream(Constants.Streams.BALL_POSSESSION, new Fields(Constants.Fields.RAW_TIMESTAMP,
                Constants.Fields.META_TEAM));
        outputFieldsDeclarer.declareStream(Constants.Streams.SHOTS_ON_GALL, new Fields(Constants.Fields.RAW_TIMESTAMP,
                Constants.Fields.META_TEAM,
                Constants.Fields.RAW_LOC_X, Constants.Fields.RAW_LOC_Y, Constants.Fields.RAW_VELOCITY,
                Constants.Fields.RAW_VEL_X, Constants.Fields.RAW_VEL_Y));
    }

    private PlayerPosition findClosestPlayer(double ballX, double ballY) {
        double closestDistance = Double.MAX_VALUE;
        PlayerPosition closestPlayer = null;
        for (PlayerPosition playerPosition : playerLocations.values()) {
            double distanceToLeftBoot = Util.calculateDistance(ballX, playerPosition.lx, ballY, playerPosition.ly);
            double distanceToRightBoot = Util.calculateDistance(ballX, playerPosition.rx, ballY, playerPosition.ry);
            double minDistance = Math.min(distanceToLeftBoot, distanceToRightBoot);
            playerPosition.distanceToBall = minDistance;
            if (closestDistance > minDistance) {
                closestDistance = minDistance;
                closestPlayer = playerPosition;
            }
        }
        return closestPlayer;
    }

}
