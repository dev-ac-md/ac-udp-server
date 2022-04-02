import java.io.IOException;
import java.net.*;

/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class PeerConnection extends DatagramServerThread {
    private static final int BUFFER_LEN = AcUdpServer.BUFFER_LEN;

    InetAddress clientAddress;
    PeerMessageDispenser peerMessageDispenser;
    ISendToAnalysis analyzerSender;
    int peerId;

    public PeerConnection(InetAddress address, int port, ISendToAnalysis analyzerSender) throws SocketException {
        super(port);
        peerId = port % 34000 % 100 % 10;
        peerMessageDispenser = new PeerMessageDispenser(this.socket, address, port);
        this.analyzerSender = analyzerSender;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public void run() {
        Logger.info("[PeerConnection] Run, port "+port);
        peerMessageDispenser.start();

        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_LEN];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
               Logger.info("[PeerConnection] peer packet received from peer "+peerId);
                analyzerSender.sendToAnalysis(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addMessage(DatagramPacket message) {
        peerMessageDispenser.addMessage(message);
    }
}