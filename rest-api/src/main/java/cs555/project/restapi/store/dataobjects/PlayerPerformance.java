package cs555.project.restapi.store.dataobjects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class PlayerPerformance {

    private String name;
    private String team;
    private double avgRunningSpeed;

    public PlayerPerformance(String name, String team) {
        this.name = name;
        this.team = team;
    }

    public void setAvgRunningSpeed(double avgRunningSpeed) {
        this.avgRunningSpeed = avgRunningSpeed;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public double getAvgRunningSpeed() {
        return avgRunningSpeed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
