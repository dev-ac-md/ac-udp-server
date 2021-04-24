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

    public PeerMessageDispenser(DatagramSocket socket) throws SocketException {
        this.socket = socket;
        this.isRunning = true;
        this.msgQueue = new ConcurrentLinkedQueue<>();
    }

    public void run() {
        while (isRunning) {
            try {
                if (!msgQueue.isEmpty()) {
                    socket.send(msgQueue.poll());
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
