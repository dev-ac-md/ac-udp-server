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
    
//    private int timeout = 100;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Syntax: DummyClient <hostname> <port>");
            return;
        }
 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
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
        String message = "welcome";
        byte[] buffer = message.getBytes();
        DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(request);

        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        socket.receive(response);
        String dataStr = new String(buffer, 0, response.getLength());
        System.out.println("received from server: "+dataStr);
        int udpServerMainPort = Integer.parseInt(dataStr);
        System.out.println("udpServerMainPort: "+udpServerMainPort);

        udpServerMainPort = 34000;
        DatagramSocket socketMainUdp = new DatagramSocket(udpServerMainPort);
        message = "x";
        buffer = message.getBytes();
        request = new DatagramPacket(buffer, buffer.length, address, udpServerMainPort);
        socketMainUdp.send(request);

        response = new DatagramPacket(buffer, buffer.length);
        socketMainUdp.receive(response);
        dataStr = new String(buffer, 0, response.getLength());
        System.out.println("received from sub-server udp_main: "+dataStr);
        int peerPort = Integer.parseInt(dataStr);
        System.out.println("peerPort: "+peerPort);
    }

    private static void processResponse(String dataStr) {
        System.out.println(dataStr);
        System.out.println();
    }
}