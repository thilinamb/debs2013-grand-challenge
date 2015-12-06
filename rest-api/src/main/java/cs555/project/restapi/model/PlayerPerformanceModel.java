package cs555.project.restapi.model;

import cs555.project.restapi.store.dataobjects.PlayerPerformance;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class PlayerPerformanceModel {
    private List<PlayerPerformance> performances;

    public List<PlayerPerformance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<PlayerPerformance> performances) {
        this.performances = performances;
    }
}
