package cs555.project.restapi.store.dataobjects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class ShotsOnGoal {
    private String team;
    private int value;

    public ShotsOnGoal(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
