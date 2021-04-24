import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class PeerMessageAnalyzer extends Thread {

    private final Queue<DatagramPacket> msgQueue;
    private boolean isRunning;
    private final ISendPacketToPeer peerSender;

    public PeerMessageAnalyzer(ISendPacketToPeer peerSender) {
        isRunning = true;
        this.peerSender = peerSender;
        this.msgQueue = new LinkedList<>();
    }

    public void run() {
        while (isRunning) {
            if (!msgQueue.isEmpty()) {
                System.out.println("processing message! ");
                processMessage(msgQueue.remove());
            }
        }
    }

    public void stopRunning() {
        isRunning = false;
    }

    public void addMessage(DatagramPacket message) {
        System.out.println("add message to queue!");
        msgQueue.add(message);
    }

    private void processMessage(DatagramPacket packet) {
        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();

        String dataStr = new String(packet.getData(), 0, packet.getLength());
        int peerId = Integer.parseInt(dataStr.substring(0,1));

        String message = dataStr.substring(1);
        System.out.println("messageAnalysis: peerId= "+peerId+", message= "+message);

        byte[] buffer = message.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        peerSender.sendPacketToPeer(response, peerId);
    }
}
