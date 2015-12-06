package cs555.project.restapi.store.dataobjects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class BallPossession {
    private String team;
    private double value;

    public BallPossession(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
