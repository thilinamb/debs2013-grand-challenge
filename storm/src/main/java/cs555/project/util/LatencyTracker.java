package cs555.project.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thilina Buddhika
 */
public class LatencyTracker {

    private static LatencyTracker instance = new LatencyTracker();
    private List<Long> values = new ArrayList<>();
    private BufferedWriter bufferedWriter;

    private LatencyTracker() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("/tmp/thilinab/lat-" +
                    Util.getMachineName() + System.currentTimeMillis() + ".stat"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LatencyTracker getInstance() {
        return instance;
    }

    public synchronized void addLatency(long latency) {
        values.add(latency);
        if (values.size() == 10) {
            try {
                for (Long v : values) {
                    bufferedWriter.write(v + "\n");
                }
                bufferedWriter.flush();
                values.clear();
            } catch (IOException e) {

            }
        }
    }

}
