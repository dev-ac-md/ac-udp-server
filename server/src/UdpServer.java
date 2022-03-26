import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.*;

/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class UdpServer extends DatagramServerThread implements ISendPacketToPeer, ISendToAnalysis {
    private static final int BUFFER_LEN = AcUdpServer.BUFFER_LEN;
    private static final int NR_PEERS=7;

    private final PeerConnection[] peerConnections;
    private int peerConnectionSize;
    private final String name;
    private final PeerMessageAnalyzer peerMessageAnalyzer;

    public UdpServer(String name, int port) throws SocketException {
        super(port);
        this.peerConnections = new PeerConnection[NR_PEERS];
        this.peerConnectionSize = 0;
        this.name = name;
        this.peerMessageAnalyzer = new PeerMessageAnalyzer(this);
        this.peerMessageAnalyzer.start();
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_LEN];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

//                String dataStr = new String(buffer, 0, request.getLength());

                PeerConnection newPeerConnection = addPeerConnection(clientAddress);
                if (newPeerConnection!=null) {
                    String message = ""+newPeerConnection.getPort();
                    buffer = message.getBytes();
                    System.out.println("[UdpServer] response message for "+ clientAddress.toString() + ": " + message+ ", buffer.length= "+buffer.length);

                    DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                    socket.send(response);
                    System.out.println("[UdpServer] response sent to "+clientAddress.toString()+":"+clientPort);
                } else {
                    System.out.println("[UdpServer] room full!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Integer getFreeIndex() {
        for (int i=0; i<peerConnections.length; i++) {
            if (peerConnections[i] == null)
                return i;
        }
        return null;
    }

    private boolean addConnectionToList(PeerConnection newPeerConnection) {
        Integer freeIndex = getFreeIndex();
        if (freeIndex!= null) {
            peerConnections[freeIndex] = newPeerConnection;
            peerConnectionSize++;
        }
        return freeIndex!= null;
    }

    private PeerConnection addPeerConnection(InetAddress address) throws SocketException {
        Integer freeIndex = getFreeIndex();
        if (freeIndex != null) {
            PeerConnection newPeerConnection = new PeerConnection(address,this.port + freeIndex + 1, this);
            addConnectionToList(newPeerConnection);
            newPeerConnection.start();
            return newPeerConnection;
        } else {
            return null;
        }
    }

    private void removeConnectionFromList(int index) {
        peerConnections[index] = null;
        peerConnectionSize--;
    }

    private void removePeerConnection() {

    }

    public String getServerName() {
        return name;
    }

    @Override
    public void sendPacketToPeer(DatagramPacket packet, int peerId) {
        if (peerConnections[peerId-1]!=null ) {
            peerConnections[peerId-1].addMessage(packet);
        }
    }

    @Override
    public void sendToAnalysis(DatagramPacket packet) {
        peerMessageAnalyzer.addMessage(packet);
    }

    public void broadcastToOthers(DatagramPacket packet, int peerId) {
        for (int i=0; i < peerConnections.length; i++) {
            if (peerConnections[i]!=null && peerId != i) {
                peerConnections[i].addMessage(packet);
            }
        }
    }
}