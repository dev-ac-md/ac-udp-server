import java.io.IOException;
import java.net.*;

/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class PeerConnection extends DatagramServerThread {
    private static final int BUFFER_LEN = 64;

    InetAddress clientAddress;
    PeerMessageDispenser peerMessageDispenser;
    ISendToAnalysis analyzerSender;

    public PeerConnection(int port, ISendToAnalysis analyzerSender) throws SocketException {
        super(port);
        System.out.println("Create PeerConnection, port= "+port);
        peerMessageDispenser = new PeerMessageDispenser(this.socket);
        this.analyzerSender = analyzerSender;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public void run() {
        peerMessageDispenser.start();

        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_LEN];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addMessage(DatagramPacket message) {
        peerMessageDispenser.addMessage(message);
    }
}