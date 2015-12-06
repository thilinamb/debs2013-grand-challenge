package cs555.project.restapi.dataaccess;

import cs555.project.restapi.store.PlayerPerfStore;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Thilina Buddhika
 */
public class PlayerPerfConsumer implements MessageListener {

    PlayerPerfStore perfStore = PlayerPerfStore.getInstance();

    @Override
    public void onMessage(Message message) {
        if (message instanceof BytesMessage) {
            BytesMessage byteMsg = (BytesMessage) message;
            try {
                int messageLength = byteMsg.readInt();
                byte[] binaryMessage = new byte[messageLength];
                byteMsg.readBytes(binaryMessage);
                process(binaryMessage);
                System.out.println("Received a message!");
            } catch (JMSException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void process(byte[] message) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(message);
        DataInputStream dis = new DataInputStream(bais);
        String playerName = dis.readUTF();
        String teamName = dis.readUTF();
        double perfVal = dis.readDouble();
        perfStore.updatePerf(playerName, teamName, perfVal);
    }

}
