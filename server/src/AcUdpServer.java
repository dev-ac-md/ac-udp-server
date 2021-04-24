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
 
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: Server <port>");
            return;
        }
 
        int port = Integer.parseInt(args[0]);
 
        try {
            DispatcherServer server = new DispatcherServer(port);
            server.start();
        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } 
    }
 
}