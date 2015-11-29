package cs555.project.possession;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
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
                if (currentBallHolder.distanceToBall <= 1000 && acceleration >= 55) {
                    // previous ball holder has hit the ball
                    System.out.println("Ball belongs to team " + currentBallHolder.team);
                }
                currentBallHolder = closestPlayer;
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

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
