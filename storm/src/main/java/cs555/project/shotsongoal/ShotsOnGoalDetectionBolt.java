package cs555.project.shotsongoal;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cs555.project.util.Constants;

/**
 * @author Thilina Buddhika
 */
public class ShotsOnGoalDetectionBolt extends BaseBasicBolt {

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
        onGoal(tuple.getStringByField(Constants.Fields.META_TEAM), projectedX, projectedY);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    public boolean onGoal(String team, double projectedX, double projectedY){
        boolean shotsOnGoal = false;
        if(team.equals("B")){
            // team 1: y > 22578.5 and y < 29898.5, x = 33941.0, z < 2440.0
            if ((projectedY >= 22578.5 && projectedY <= 29898.5) && (projectedX >= 33941)){
                shotsOnGoal = true;
                System.out.println("It's a shot on goal by team B!");
            }
        } else {
            // team 2: y > 22560.0 and y < 29880.0, x = -33968.0, z < 2440.0
            if ((projectedY >= 22560 && projectedY <= 29880) && (projectedX >= -33968)){
                shotsOnGoal = true;
                System.out.println("It's a shot on goal by team A!");
            }
        }
        return shotsOnGoal;
    }
}
