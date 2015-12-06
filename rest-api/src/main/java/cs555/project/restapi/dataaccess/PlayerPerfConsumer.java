package cs555.project.restapi.dataaccess;

import cs555.project.restapi.store.PlayerPerfStore;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Thilina Buddhika
 */
public class PlayerPerfConsumer extends AbstractConsumer {

    PlayerPerfStore perfStore = PlayerPerfStore.getInstance();

    protected void process(DataInputStream dis) throws IOException {
        String playerName = dis.readUTF();
        String teamName = dis.readUTF();
        double perfVal = dis.readDouble();
        perfStore.updatePerf(playerName, teamName, perfVal);
    }

}
