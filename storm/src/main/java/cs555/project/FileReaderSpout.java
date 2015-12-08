package cs555.project;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import cs555.project.util.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;


/**
 * Reads from the file and play the tuples based on the timestamp
 *
 * @author Thilina Buddhika
 */
public class FileReaderSpout extends BaseRichSpout {

    private class FileReaderThread implements Runnable {
        @Override
        public void run() {
            try {
                String line;
                String[] currentSegments = null;
                long currentTs = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    if (currentTs == 0) { // this is the first line
                        currentSegments = line.split(",");
                    }
                    long timeInPicoSecs = Long.parseLong(currentSegments[1]);
                    currentTs = timeInPicoSecs / Constants.PICO_TO_MILLI;
                    // emit the line
                    if (isWithinTheDuration(timeInPicoSecs)) {
                        FileReaderSpout.this.addToQueue(new Values(System.currentTimeMillis(),
                                Integer.parseInt(currentSegments[0]), currentTs,
                                Double.parseDouble(currentSegments[2]), Double.parseDouble(currentSegments[3]),
                                Double.parseDouble(currentSegments[4]),
                                Double.parseDouble(currentSegments[5]), Double.parseDouble(currentSegments[6]),
                                Double.parseDouble(currentSegments[7]), Double.parseDouble(currentSegments[8]),
                                Double.parseDouble(currentSegments[9]),
                                Double.parseDouble(currentSegments[10]), Double.parseDouble(currentSegments[11]),
                                Double.parseDouble(currentSegments[12])));
                    }
                    // read next line
                    line = bufferedReader.readLine();
                    if (line == null) { // EOF
                        break;
                    }
                    currentSegments = line.split(",");
                    long nextTs = Long.parseLong(currentSegments[1]) / Constants.PICO_TO_MILLI;
                    long timeUntilNextRecord = nextTs - currentTs;
                    Thread.sleep(timeUntilNextRecord);
                }
            } catch (java.io.IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    BufferedReader bufferedReader;
    SpoutOutputCollector collector;
    private Queue<Values> recordQueue = new ArrayDeque<>();

    private synchronized int addToQueue(Values vals) {
        recordQueue.add(vals);
        return recordQueue.size();
    }

    private synchronized Values readFromQueue() {
        return recordQueue.poll();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(Constants.Fields.EMIT_TS, Constants.Fields.RAW_SID,
                Constants.Fields.RAW_TIMESTAMP,
                Constants.Fields.RAW_LOC_X, Constants.Fields.RAW_LOC_Y, Constants.Fields.RAW_LOC_Z,
                Constants.Fields.RAW_VELOCITY, Constants.Fields.RAW_ACCELERATION,
                Constants.Fields.RAW_VEL_X, Constants.Fields.RAW_VEL_Y, Constants.Fields.RAW_VEL_Z,
                Constants.Fields.RAW_ACC_X, Constants.Fields.RAW_ACC_Y, Constants.Fields.RAW_ACC_Z));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        try {
            bufferedReader = new BufferedReader(new FileReader((String) map.get(Constants.INPUT_FILE)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(new FileReaderThread()).start();
    }


    @Override
    public void nextTuple() {
        Values nextMessage = readFromQueue();
        if(nextMessage != null) {
            String handle = UUID.randomUUID().toString();
            collector.emit(nextMessage, handle);
        }
    }

    private boolean isWithinTheDuration(long ts) {
        boolean valid = false;
        //System.out.println(ts);
        if (ts >= 10753295594424116l && ts <= 12557295594424116l) {
            valid = true;
        } else if (ts >= 13086639146403495l && ts <= 14879639146403495l) {
            valid = true;
        }
        return valid;
    }

}
