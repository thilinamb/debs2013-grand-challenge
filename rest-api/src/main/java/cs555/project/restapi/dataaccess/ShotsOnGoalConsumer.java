package cs555.project.restapi.dataaccess;

import cs555.project.restapi.store.ShotsOnGoalStore;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Thilina Buddhika
 */
public class ShotsOnGoalConsumer extends AbstractConsumer {

    private ShotsOnGoalStore dataStore = ShotsOnGoalStore.getInstance();

    @Override
    protected void process(DataInputStream dataInputStream) throws IOException {
        int teamA = dataInputStream.readInt();
        int teamB = dataInputStream.readInt();
        dataStore.update(teamA, teamB);
        System.out.println("updated shots-on-goal data store!");
    }

}
