/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient.handlers;

import cloxclient.Client;
import cloxclient.helpers.ClientList;
import cloxclient.models.Message;
import cloxclient.models.Messages;
import cloxclient.ui.ClientsList;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lib.MessageDialog;

/**
 *
 * @author segun
 */
public class ProtocolHandler extends Thread {

    Socket socket;
    //CloxClient mclient;
    public static File ackFile;
    ObjectProperty<Object[]> clientNames = new SimpleObjectProperty<Object[]>();
    Stage primaryStage;

    public ProtocolHandler(Socket socket, Stage primaryStage) {
        this.socket = socket;
        this.primaryStage = primaryStage;
        Bindings.bindBidirectional(clientNames, ClientsList.clients);
        //this.mclient = client;
    }

    public void stopThread() {
        try {
            socket.close();
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null, e);
        }
    }

    private Message parseMessage(String clientMessage) {
        Message message = new Message();
        String[] splitted = clientMessage.split(":s");
        for (String token : splitted) {
            if (token.contains("from")) {
                message.setFrom(token.substring((token.indexOf("=") + 1), token.length()));
            }
            if (token.contains("to")) {
                message.setTo(token.substring((token.indexOf("=") + 1), token.length()));
            }
            if (token.contains("date")) {
                Long dateInMillis = Long.parseLong(token.substring((token.indexOf("=") + 1), token.length()));
                Date date = new Date(dateInMillis);
                message.setTime(date);
            }
            if (token.contains("msg")) {
                message.setMsg(token.substring((token.indexOf("=") + 1), token.length()));
            }
        }
        return message;
    }

    private boolean updateClientList(String newClientList) {
        String[] splitted = newClientList.split(",");
        ArrayList<String> clientNames = new ArrayList<String>();
        for (String oneClient : splitted) {
            clientNames.add(oneClient);
        }
        ClientList.clientList = new ArrayList<String>();
        ClientList.clientList = clientNames;
        return true;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String serverMessage = "";
                while ((serverMessage = reader.readLine()) != null) {
                    if (serverMessage != null) {
                        if (serverMessage.startsWith("clients")) {
                            serverMessage = serverMessage.replace("clients:", "");
                            updateClientList(serverMessage);
                            Object[] names = ClientList.clientList.toArray();
                            clientNames.set(names);
                        } else if (serverMessage.startsWith("message")) {
                            String message = serverMessage;

                            message = message.replace("message:", "");

                            Message parsed = parseMessage(message);

                            int len = 0;
                            Message[] newMessages = new Message[1];

                            if (Messages.list.get() != null) {
                                len = Messages.list.get().length;
                                newMessages = Arrays.copyOf(Messages.list.get(), len + 1);
                            }

                            newMessages[len] = parsed;

                            Messages.list.set(newMessages);
                        } else if (serverMessage.startsWith("ackfile")) {
                            serverMessage = serverMessage.replace("ackfile:", "");
                            String fp[] = serverMessage.split(":s");
                            int port = 0;

                            for (String each : fp) {
                                if (each.startsWith("port")) {
                                    port = Integer.parseInt(each.replace("port=", ""));
                                    break;
                                }
                            }

                            System.out.println("PORT = " + port);
                            Socket socket1 = new Socket(Client.host, port);
                            OutputStream out = socket1.getOutputStream();
                            FileInputStream fis = new FileInputStream(ackFile);
                            int ch;

                            while ((ch = fis.read()) != -1) {
                                out.write(ch);
                            }
                            out.flush();
                            out.close();
                            MessageDialog.showMessageDialog(primaryStage, "File Transfer Successful");
                        } else if (serverMessage.startsWith("file")) {
                            serverMessage = serverMessage.replace("file:", "");
                            String fp[] = serverMessage.split(":s");
                            int port = 0;
                            String filename = "";
                            String from = "";

                            for (String each : fp) {
                                if (each.startsWith("port")) {
                                    port = Integer.parseInt(each.replace("port=", ""));
                                }
                                if (each.startsWith("filename")) {
                                    filename = each.replace("filename=", "");
                                }
                                if (each.startsWith("from")) {
                                    from = each.replace("from=", "");
                                }
                            }

                            final int thePort = port;
                            System.out.println("PORT = " + port + "filename: " + filename + " from = " + from);

                            MessageDialog.showConfirmDialog(primaryStage,
                                    from + " is trying to send you a file. "
                                    + "\n Filename: " + filename
                                    + "\n Do you want to accept it?",
                                    new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent event) {
                                            MessageDialog.stage.hide();
                                            Button b = (Button) event.getSource();
                                            if (b.getText().equals("Yes")) {
                                                FileChooser fileChooser = new FileChooser();
                                                File f = fileChooser.showSaveDialog(primaryStage);
                                                if (f != null) {
                                                    Socket socket1;
                                                    try {
                                                        socket1 = new Socket(Client.host, thePort);
                                                        FileOutputStream fos = new FileOutputStream(f);
                                                        InputStream is = socket1.getInputStream();

                                                        int ch;

                                                        while ((ch = is.read()) != -1) {
                                                            fos.write(ch);
                                                        }
                                                        fos.flush();
                                                        fos.close();
                                                        MessageDialog.showMessageDialog(primaryStage, "File transafer succesful. File Saved!");
                                                    } catch (UnknownHostException ex) {
                                                        Logger.getLogger(ProtocolHandler.class.getName()).log(Level.SEVERE, null, ex);
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(ProtocolHandler.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            }
                                        }
                                    });
                        } else {
                            System.out.println(serverMessage);
                        }
                    } else {
                        break;
                    }
                }
                Thread.sleep(2000);
            } catch (Exception ioe) {
                Logger.getAnonymousLogger().log(Level.SEVERE, null, ioe);
                try {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                }
                break;
            }
        }
        /**
         * mclient.btnLogin.setEnabled(true); mclient.btnSend.setEnabled(false);
         * mclient.btnDisconnect.setEnabled(false);
         * mclient.lblStatus.setText("Disconnected"); Object names[] = new
         * Object[0]; mclient.setNames(names);
         * mclient.namesList.setSelectedIndices(new int[0]); *
         */
    }
}
