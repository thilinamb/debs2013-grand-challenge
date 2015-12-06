package cs555.project.restapi.model;

import cs555.project.restapi.store.dataobjects.ShotsOnGoal;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class ShotsOnGoalModel {

    private List<ShotsOnGoal> shotsOnGoalData = new ArrayList<>();

    public List<ShotsOnGoal> getShotsOnGoalData() {
        return shotsOnGoalData;
    }

    public void setShotsOnGoalData(List<ShotsOnGoal> shotsOnGoalData) {
        this.shotsOnGoalData = shotsOnGoalData;
    }
}
