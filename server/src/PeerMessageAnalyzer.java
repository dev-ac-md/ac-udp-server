import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Queue;

/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class PeerMessageAnalyzer extends Thread {

    private Queue<DatagramPacket> msgQueue;
    private boolean isRunning;
    private ISendPacketToPeer peerSender;

    public PeerMessageAnalyzer(ISendPacketToPeer peerSender) throws SocketException {
        isRunning = true;
        this.peerSender = peerSender;
    }

    public void run() {
        while (isRunning) {
            if (!msgQueue.isEmpty()) {
                processMessage(msgQueue.remove());
            }
        }
    }

    public void stopRunning() {
        isRunning = false;
    }

    public void addMessage(DatagramPacket message) {
        msgQueue.add(message);
    }

    private void processMessage(DatagramPacket packet) {
        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();

        String dataStr = new String(packet.getData(), 0, packet.getLength());
        int peerId = Integer.parseInt(dataStr.substring(0,1));

        String message = dataStr.substring(1);
        byte[] buffer = message.getBytes();

        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        peerSender.sendPacketToPeer(response, peerId);
    }
}
