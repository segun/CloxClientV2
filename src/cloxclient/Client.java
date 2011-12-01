/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient;

import cloxclient.handlers.ProtocolHandler;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 *
 * @author segun
 */
public class Client extends Thread {

    public static int port;
    public static String host;
    public static boolean useProperties;
    public static String username;
    public static boolean STOP_CLIENT = false;
    public Socket clientSocket;
    ProtocolHandler protocolHandler;
    Stage primaryStage;

    public Client(String username, Stage primaryStage) {
        Client.username = username;
        STOP_CLIENT = false;
    }

    public void disconnect() {
        protocolHandler.stopThread();
    }
    

    @Override
    public void run() {
        //wait until you see a connection
        while (!STOP_CLIENT) {
            try {
                clientSocket = new Socket(host, port);

                String clientDetails = "details:port=" + clientSocket.getLocalPort() + ":sname=" + username + ":saddress=" + clientSocket.getLocalAddress();
                System.out.println(clientDetails);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                writer.write(clientDetails + "\n");
                writer.flush();
                
                System.out.println("flushed");
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        protocolHandler = new ProtocolHandler(clientSocket, primaryStage);
                        protocolHandler.start();
                    }
                });                
                break;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
