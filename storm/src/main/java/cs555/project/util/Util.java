package cs555.project.util;

/**
 * @author Thilina Buddhika
 */
public class Util {
    public static double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.abs(Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2)));
    }
}
