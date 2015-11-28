package cs555.project.running;

import cs555.project.util.SlidingWindowEntry;

/**
 * @author Thilina Buddhika
 */
public class DeltaDistanceSWEntry implements SlidingWindowEntry {

    private long timeStart;
    private double distance;
    private double x;
    private double y;

    public DeltaDistanceSWEntry(long timeStart, double distance, double x, double y) {
        this.timeStart = timeStart;
        this.distance = distance;
        this.x = x;
        this.y = y;
    }

    @Override
    public long getStartTime() {
        return timeStart;
    }

    public double getDistance() {
        return distance;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
