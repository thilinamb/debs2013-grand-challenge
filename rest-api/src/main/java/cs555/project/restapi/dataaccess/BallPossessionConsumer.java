package cs555.project.restapi.dataaccess;

import cs555.project.restapi.store.BallPossessionInfoStore;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Thilina Buddhika
 */
public class BallPossessionConsumer extends AbstractConsumer {

    private BallPossessionInfoStore dataStore = BallPossessionInfoStore.getInstance();

    @Override
    protected void process(DataInputStream dis) throws IOException {
        double teamAPossession = dis.readDouble();
        double teamBPossession = dis.readDouble();
        dataStore.update(teamAPossession, teamBPossession);
        System.out.println("Updated ball possession store.");
    }
}
