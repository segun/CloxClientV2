/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient.handlers;

import cloxclient.models.Message;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author segun
 */
public class Messenger {

    public static void sendMessage(Message message, Socket socket) {
        String sendThis = "message:from=" + message.getFrom() + ":s" +
                "to=" + message.getTo() + ":s" +
                "date=" + message.getTime().getTime() + ":s" +
                "msg=" + message.getMsg() + "\n";
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(sendThis);
            writer.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Message produceMessage(String msg, String from, String to, Date time) {
        Message newMessage = new Message();
        newMessage.setFrom(from);
        newMessage.setTime(time);
        newMessage.setTo(to);
        newMessage.setMsg(msg);
        return newMessage;
    }

    public static void sendMessage(String message, Socket socket) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(message);
            writer.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void sendFile(File f, Socket socket, String to) {
        String filename = "file:filename=" + f.getName() + ":sto=" + to + "\n";
        ProtocolHandler.ackFile = f;
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(filename);
            writer.flush();            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
