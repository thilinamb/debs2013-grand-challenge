package cs555.project.util;

import java.util.List;

/**
 * @author Thilina Buddhika
 */
public interface SlidingWindowCallback {
    public void remove(List<SlidingWindowEntry> entries);
}
