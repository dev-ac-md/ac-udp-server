import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class PeerMessageDispenser extends Thread {
    private static final int BUFFER_LEN = 64;

    private final DatagramSocket socket;
    private final Queue<DatagramPacket> msgQueue;
    private boolean isRunning;
    private final InetAddress address;
    private final int port;

    public PeerMessageDispenser(DatagramSocket socket, InetAddress address, int port){
        this.socket = socket;
        this.isRunning = true;
        this.msgQueue = new ConcurrentLinkedQueue<>();
        this.address = address;
        this.port = port;
    }

    public void run() {
        while (isRunning) {
            try {
                if (!msgQueue.isEmpty()) {
                    DatagramPacket packet = msgQueue.poll();
                    DatagramPacket newPacket = new DatagramPacket(packet.getData(), packet.getLength(), this.address, this.port);
                    socket.send(newPacket);
                    System.out.println("peer packet sent to "+newPacket.getAddress()+":"+port+" "+new String(newPacket.getData(), 0, newPacket.getLength()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRunning() {
        isRunning = false;
    }

    public void addMessage(DatagramPacket message) {
        msgQueue.add(message);
    }
}
