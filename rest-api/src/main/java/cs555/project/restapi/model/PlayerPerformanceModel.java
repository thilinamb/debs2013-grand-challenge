package cs555.project.restapi.model;

import cs555.project.restapi.store.dataobjects.PlayerPerformance;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class PlayerPerformanceModel {
    private Map<String, PlayerPerformance> performances;

    public Map<String, PlayerPerformance> getPerformances() {
        return performances;
    }

    public void setPerformances(Map<String, PlayerPerformance> performances) {
        this.performances = performances;
    }
}
