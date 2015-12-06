package cs555.project.restapi.dataaccess;

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
public abstract class AbstractConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message instanceof BytesMessage) {
            BytesMessage byteMsg = (BytesMessage) message;
            try {
                int messageLength = byteMsg.readInt();
                byte[] binaryMessage = new byte[messageLength];
                byteMsg.readBytes(binaryMessage);
                parse(binaryMessage);
            } catch (JMSException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parse(byte[] binaryPayload) throws IOException {
        ByteArrayInputStream bais = null;
        DataInputStream dis = null;
        try {
            bais = new ByteArrayInputStream(binaryPayload);
            dis = new DataInputStream(bais);
            process(dis);
        } finally {
            if (bais != null) {
                bais.close();
            }
            if (dis != null) {
                dis.close();
            }
        }
    }

    protected abstract void process(DataInputStream dataInputStream) throws IOException;
}
