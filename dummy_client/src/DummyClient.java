import java.io.*;
import java.net.*;
import java.util.Date;
 
/**
 * This program demonstrates how to implement a UDP client program.
 *
 *
 * @author www.codejava.net
 */
public class DummyClient {
    static final int BUFFER_SIZE = 64;
    static int id;
//    private int timeout = 100;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Syntax: DummyClient <hostname> <port> <id>");
            return;
        }
 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        id  = Integer.parseInt(args[2]);
 
        try {
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket(port);

            test1(socket, address, port);
//            while (true) {
//
//                DatagramPacket request = new DatagramPacket(new byte[1], 1, address, port);
//                socket.send(request);
//                // long timeSend = date.getTime();
//                // while (date.getTime() - timeSend < timeout) {
//                //     Thread.sleep(timeout/10);
//                //     socket.receive(response);
//                // }
//
//                byte[] buffer = new byte[512];
//                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
//                socket.receive(response);
//                String dataStr = new String(buffer, 0, response.getLength());
//                processResponse(dataStr);
//                Thread.sleep(200);
//            }
 
        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void test1(DatagramSocket socket,InetAddress address, int port) throws IOException {
        sendMessage("welcome", socket, address, port);
        String dataStr = receiveMessage(socket);

        int udpServerMainPort = Integer.parseInt(dataStr);

        System.out.println("received from server: "+dataStr);
        System.out.println("udpServerMainPort: "+udpServerMainPort);

        DatagramSocket socketMainUdp = new DatagramSocket(udpServerMainPort);
        sendMessage("x", socketMainUdp, address, udpServerMainPort);
        dataStr = receiveMessage(socketMainUdp);

        int peerPort = Integer.parseInt(dataStr);

        System.out.println("received from sub-server udp_main: "+dataStr);
        System.out.println("peerPort: "+peerPort);

        DatagramSocket socketPeer = new DatagramSocket(peerPort);

        if (id==1) {
            testPeer1(socketPeer, address, peerPort);
        } else {
            testPeer2(socketPeer, address, peerPort);
        }
    }

    private static void testPeer1(DatagramSocket socket,InetAddress address, int port) throws IOException {
//        sendMessage("0self_test", socket, address, port);
        while(true) {
            String dataStr = receiveMessage(socket);
            System.out.println("received from peer: "+dataStr);
            sendMessage("1ok_you_can_join", socket, address, port);
        }
    }

    private static void testPeer2(DatagramSocket socket,InetAddress address, int port) throws IOException {
        sendMessage("0try_join: dev", socket, address, port);
        String dataStr = receiveMessage(socket);

        System.out.println("received from peer: "+dataStr);
    }

    private static void sendMessage(String message, DatagramSocket socket,InetAddress address, int port) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(request);
    }

    private static String receiveMessage(DatagramSocket socket) throws IOException {
        DatagramPacket response = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        socket.receive(response);
        return new String(response.getData(), 0, response.getLength());
    }

    private static void processResponse(String dataStr) {
        System.out.println(dataStr);
        System.out.println();
    }
}