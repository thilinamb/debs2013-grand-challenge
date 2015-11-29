package cs555.project;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import cs555.project.possession.BallHitDetectionBolt;
import cs555.project.running.RunningPerfCalcBolt;
import cs555.project.shotsongoal.ShotsOnGoalDetectionBolt;
import cs555.project.util.Constants;

/**
 * @author Thilina Buddhika
 */
public class MainTopology {

    public static final String FILE_READER_SPOUT = "file-reader-spout";
    public static final String ENRICHMENT_BOLT = "enrichment-bolt";
    public static final String HUB_BOLT = "hub-bolt";
    public static final String RUNNING_PERF_CALC_BOLT = "running-perf-calc-bolt";
    public static final String BALL_HIT_DETECTION_BOLT = "bolt-hit-detection-bolt";
    public static final String SHOTS_ON_GOAL_DETECTION_BOLT = "shots-on-goal-detection-bolt";

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        // FileReaderBolt -> Enrichment Bolt -> Hub bolt
        builder.setSpout(FILE_READER_SPOUT, new FileReaderSpout(), 1);
        builder.setBolt(ENRICHMENT_BOLT, new EnrichBolt(), 1).globalGrouping(FILE_READER_SPOUT);
        builder.setBolt(HUB_BOLT, new HubBolt(), 1).globalGrouping(ENRICHMENT_BOLT);

        // add topology 1 - player running perf.
        // partition the stream by player's name
        builder.setBolt(RUNNING_PERF_CALC_BOLT, new RunningPerfCalcBolt(), 4).fieldsGrouping(HUB_BOLT,
                Constants.Streams.PLAYER_POSITIONS, new Fields(Constants.Fields.META_NAME));

        // add topology 2 - ball possession
        builder.setBolt(BALL_HIT_DETECTION_BOLT, new BallHitDetectionBolt()).globalGrouping(HUB_BOLT,
                Constants.Streams.PLAYER_BALL_POSITIONS);

        // add topology 3 - shots on goal
        builder.setBolt(SHOTS_ON_GOAL_DETECTION_BOLT, new ShotsOnGoalDetectionBolt()).globalGrouping(
                BALL_HIT_DETECTION_BOLT, Constants.Streams.SHOTS_ON_GALL);

        Config conf = new Config();
        conf.put(Config.TOPOLOGY_DEBUG, false);

        // run on Storm cluster
        if (args != null && !args[0].equalsIgnoreCase("local")) {
            conf.setNumWorkers(4);
            conf.put(Constants.INPUT_FILE, args[1]);
            try {
                StormSubmitter.submitTopology("soccer-field-analysis", conf, builder.createTopology());
            } catch (AlreadyAliveException | InvalidTopologyException e) {
                e.printStackTrace();
            }
        // local mode
        } else {
            conf.setMaxTaskParallelism(5);
            LocalCluster cluster = new LocalCluster();
            conf.put(Constants.INPUT_FILE, args[1]);
            cluster.submitTopology(args[0], conf, builder.createTopology());
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cluster.shutdown();
        }
    }
}
