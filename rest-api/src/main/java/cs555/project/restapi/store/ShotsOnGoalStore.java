package cs555.project.restapi.store;

import cs555.project.restapi.store.dataobjects.ShotsOnGoal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thilina Buddhika
 */
public class ShotsOnGoalStore {

    private static final ShotsOnGoalStore instance = new ShotsOnGoalStore();

    private ShotsOnGoal teamA;
    private ShotsOnGoal teamB;
    private List<ShotsOnGoal> values;

    private ShotsOnGoalStore(){
        teamA = new ShotsOnGoal("A");
        teamB = new ShotsOnGoal("B");
        values = new ArrayList<>();
        values.add(teamA);
        values.add(teamB);
    }

    public static ShotsOnGoalStore getInstance(){
        return instance;
    }

    public synchronized void update(int teamA, int teamB){
        this.teamA.setValue(teamA);
        this.teamB.setValue(teamB);
    }

    public synchronized List<ShotsOnGoal> getValues(){
        return values;
    }
}
