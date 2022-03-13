import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.*;

/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class DispatcherServer extends DatagramServerThread {
    private static final int BUFFER_LEN = 64;

    List<UdpServer> udpServerList;

    public DispatcherServer(int port) throws SocketException {
        super(port);
        udpServerList = new ArrayList<>();
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_LEN];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                String dataStr = new String(buffer, 0, request.getLength());
                System.out.println("[DispatcherServer] received message from client: "+dataStr);

                UdpServer newUdpServer = addUdpServer(dataStr);
                if (newUdpServer!=null) {
                    String message = ""+newUdpServer.getPort();
                    buffer = message.getBytes();

                    DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                    socket.send(response);
                    System.out.println("[DispatcherServer] response sent: "+message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    private UdpServer addUdpServer(String serverName) throws SocketException {
        if (udpServerList.stream().noneMatch(s->s.getServerName().equals(serverName)) ) {
            UdpServer newServer = new UdpServer(serverName, 34000 + udpServerList.size() * 10);
            udpServerList.add(newServer);
            newServer.start();
            return newServer;
        } else {
            return udpServerList.stream().filter(s->s.getServerName().equals(serverName)).findAny().orElse(null);
        }
    }
}