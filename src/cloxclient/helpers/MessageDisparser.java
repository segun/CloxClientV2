/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient.helpers;

import cloxclient.Client;
import cloxclient.models.Message;
import cloxclient.models.Messages;
import cloxclient.ui.ChatWindow;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

/**
 *
 * @author trinisoftinc
 */
public class MessageDisparser implements ChangeListener<Message[]> {

    ObjectProperty<Message[]> messagesPropertyList = new SimpleObjectProperty<Message[]>();
    HashMap<String, ChatWindow> chats = new HashMap<String, ChatWindow>();
    Stage primaryState;
    Client client;
    String me;

    public MessageDisparser(Stage primaryStage, Client client, String me) {
        this.primaryState = primaryStage;
        this.client = client;
        this.me = me;
        init();
    }

    private void init() {
        Bindings.bindBidirectional(messagesPropertyList, Messages.list);
        messagesPropertyList.addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends Message[]> observable, Message[] oldValue, final Message[] newValue) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                for (Message msg : newValue) {
                    if (!msg.isIsShown()) {
                        String chattingWith = msg.getFrom();
                        if (msg.getFrom().equals(me)) {
                            chattingWith = msg.getTo();
                        }
                        ChatWindow chatWindow = chats.get(chattingWith);
                        if (chatWindow == null) {
                            chatWindow = new ChatWindow(primaryState, client, newValue, chattingWith, me);
                            chats.put(chattingWith, chatWindow);
                        } else {
                            chatWindow.setMessages(newValue);
                        }
                        msg.setIsShown(true);
                        chatWindow.show();
                    }
                }
            }
        });
    }

    public HashMap<String, ChatWindow> getChats() {
        return chats;
    }

    public void setChats(HashMap<String, ChatWindow> chats) {
        this.chats = chats;
    }

    public void setClient(Client client) {
        this.client = client;
        for (String a : chats.keySet()) {
            ChatWindow w = chats.get(a);
            if (w != null) {
                w.setClient(client);
            }
        }
    }

    public void setMe(String me) {
        this.me = me;
    }
}
