package cs555.project.restapi.store;

import cs555.project.restapi.store.dataobjects.BallPossession;

/**
 * @author Thilina Buddhika
 */
public class BallPossessionInfoStore {

    private static BallPossessionInfoStore instance = new BallPossessionInfoStore();
    private BallPossession teamA = new BallPossession("A");
    private BallPossession teamB = new BallPossession("B");

    private BallPossessionInfoStore(){

    }

    public static BallPossessionInfoStore getInstance(){
        return instance;
    }

    public synchronized void update(double teamAPossession, double teamBPossession){
        this.teamA.setValue(teamAPossession);
        this.teamB.setValue(teamBPossession);
    }


}
