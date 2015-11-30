package cs555.project.util;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * @author Thilina Buddhika
 */
public class SlidingWindow {
    private ArrayDeque<SlidingWindowEntry> window = new ArrayDeque<>();
    private long tsStart;
    private long tsEnd;
    private long length;

    public SlidingWindow(long length) {
        this.length = length;
    }

    public void add(SlidingWindowEntry entry, SlidingWindowCallback callback) {
        //System.out.println("Adding " + entry.getStartTime());
        // very first entry in the window
        if (tsStart == 0l) {
            tsStart = entry.getStartTime();
        }
        // add the entry
        window.addLast(entry);
        // sliding window should be moved.
        if (entry.getStartTime() > tsEnd) {
            // update the timestamp end timestamp
            tsEnd = entry.getStartTime();
            // now we need to remove the entries which are expired
            long newTsStart = tsEnd - length + 1;
            ArrayList<SlidingWindowEntry> removed = new ArrayList<>();
            while (tsStart < newTsStart) {
                if (window.element().getStartTime() < newTsStart) {
                    removed.add(window.removeFirst());
                    tsStart = window.element().getStartTime();
                }
            }
            callback.remove(removed);
        }
    }

    public SlidingWindowEntry getLastEntry(){
        return window.getLast();
    }
}