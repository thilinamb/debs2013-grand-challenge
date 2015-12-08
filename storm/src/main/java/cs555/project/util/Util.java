package cs555.project.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Thilina Buddhika
 */
public class Util {
    public static double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.abs(Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2)));
    }

    public static String getMachineName() {
        InetAddress inetAddr;
        try {
            inetAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            inetAddr = InetAddress.getLoopbackAddress();
        }
        return inetAddr.getHostName();
    }
}
