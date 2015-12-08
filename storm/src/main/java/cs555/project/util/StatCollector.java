package cs555.project.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton stat collector to calculate cumulative throughput at
 * a worker.
 *
 * @author Thilina Buddhika
 */
public class StatCollector{

    private static StatCollector instance = new StatCollector();
    public static final int THRESHOLD = 20000;

    private AtomicLong msgCounter = new AtomicLong(0);
    private long lastRecordedTime = -1;
    private int multiplier = 1;
    private long previousCount = 0;
    private BufferedWriter bufferedWriter;

    private StatCollector() {

    }

    public static StatCollector getInstance() {
        return instance;
    }

    public synchronized void record(int delta) {
        try {
            if (lastRecordedTime > 0) {
                long oldVal = msgCounter.get();
                long current = msgCounter.addAndGet(delta);
                long threshold = (multiplier * THRESHOLD);
                if ((oldVal < threshold) && (threshold <= current)) {
                    long currentTimeStamp = System.currentTimeMillis();
                    long timeElapsed = currentTimeStamp - lastRecordedTime;
                    bufferedWriter.write(((double) (current - previousCount) * 1000) / timeElapsed + "\n");
                    bufferedWriter.flush();
                    lastRecordedTime = currentTimeStamp;
                    multiplier++;
                    previousCount = current;
                }
            } else {
                lastRecordedTime = System.currentTimeMillis();
                bufferedWriter = new BufferedWriter(new FileWriter(
                        "/tmp/thilinamb/storm-" + getMachineName() + "-" + lastRecordedTime + ".stat"));
                msgCounter.addAndGet(delta);
                previousCount = delta;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMachineName() {
        InetAddress inetAddr;
        try {
            inetAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            inetAddr = InetAddress.getLoopbackAddress();
        }
        return inetAddr.getHostName();
    }
}

