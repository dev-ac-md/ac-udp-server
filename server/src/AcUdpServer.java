import java.io.*;
import java.net.*;
import java.util.*;
 
/**
 * This program demonstrates how to implement a UDP server program.
 *
 *
 * @author dev-ac-md
 */
public class AcUdpServer {
    public static final int BUFFER_LEN = 8192;

    public static void main(String[] args) {
        if (args.length < 1) {
            Logger.error("Syntax: Server <port>");
            return;
        }
 
        int port = Integer.parseInt(args[0]);

        if (args.length >=2) {
            Logger.setPriority(args[1]);
        }
 
        try {
            DispatcherServer server = new DispatcherServer(port);
            server.start();
        } catch (SocketException ex) {
            Logger.error("Socket error: " + ex.getMessage());
        } 
    }
 
}