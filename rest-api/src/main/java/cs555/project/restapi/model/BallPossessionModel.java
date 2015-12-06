package cs555.project.restapi.model;

import cs555.project.restapi.store.dataobjects.BallPossession;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thilina Buddhika
 */
@XmlRootElement
public class BallPossessionModel {

    private List<BallPossession> ballPossessionInfo = new ArrayList<>();

    public List<BallPossession> getBallPossessionInfo() {
        return ballPossessionInfo;
    }

    public void setBallPossessionInfo(List<BallPossession> ballPossessionInfo) {
        this.ballPossessionInfo = ballPossessionInfo;
    }
}
