package cs555.project.restapi.store;

import cs555.project.restapi.store.dataobjects.ShotsOnGoal;

/**
 * @author Thilina Buddhika
 */
public class ShotsOnGoalStore {

    private static final ShotsOnGoalStore instance = new ShotsOnGoalStore();

    private ShotsOnGoal teamA = new ShotsOnGoal("A");
    private ShotsOnGoal teamB = new ShotsOnGoal("B");

    private ShotsOnGoalStore(){}

    public static ShotsOnGoalStore getInstance(){
        return instance;
    }

    public synchronized void update(int teamA, int teamB){
        this.teamA.setValue(teamA);
        this.teamB.setValue(teamB);
    }

    public synchronized void getValues(){

    }
}
