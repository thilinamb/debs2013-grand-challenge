package cs555.project.restapi.store;

import cs555.project.restapi.store.dataobjects.BallPossession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thilina Buddhika
 */
public class BallPossessionInfoStore {

    private static BallPossessionInfoStore instance = new BallPossessionInfoStore();
    private BallPossession teamA;
    private BallPossession teamB;
    private List<BallPossession> values;

    private BallPossessionInfoStore(){
        teamA = new BallPossession("A");
        teamB = new BallPossession("B");
        values = new ArrayList<>();
        values.add(teamA);
        values.add(teamB);
    }

    public static BallPossessionInfoStore getInstance(){
        return instance;
    }

    public synchronized void update(double teamAPossession, double teamBPossession){
        this.teamA.setValue(teamAPossession);
        this.teamB.setValue(teamBPossession);
    }

    public synchronized List<BallPossession> getValues(){
        return values;
    }
}
