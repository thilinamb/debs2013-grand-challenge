package cs555.project.restapi.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class PlayerPerformance {
    private Map<String, Double> performances;

    public Map<String, Double> getPerformances() {
        return performances;
    }

    public void setPerformances(Map<String, Double> performances) {
        this.performances = performances;
    }
}
