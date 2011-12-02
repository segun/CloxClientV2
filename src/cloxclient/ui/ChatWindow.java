/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient.ui;

import cloxclient.Client;
import cloxclient.handlers.Messenger;
import cloxclient.models.Message;
import cloxclient.models.Messages;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.*;

/**
 *
 * @author trinisoftinc
 */
public class ChatWindow {

    public static final int STAGE_W = 670;
    public static final int STAGE_H = 500;
    ListView<BorderPane> messagesView;
    ScrollPane messagesScrollPane;
    TextArea messageToSendArea;
    Button sendFileButton, copySelectedButton;
    Stage chatWindowStage = new Stage(),
            primaryStage;
    private Message[] messages;
    Client client;
    String toName;
    String me;

    public ChatWindow(Stage primaryStage, Client client, Message[] message, String toName, String me) {
        this.primaryStage = primaryStage;
        this.client = client;
        this.messages = message;
        this.toName = toName;
        this.me = me;
        init();
    }

    public void show() {
        showMessages();
        chatWindowStage.show();
    }

    private void showMessages() {
        List<BorderPane> formattedMessages = new ArrayList<BorderPane>();
        if (messages != null) {
            for (Message aMessage : messages) {
                if (aMessage.getFrom().equals(toName) || aMessage.getTo().equals(toName)) {
                    formattedMessages.add(constructBorderPaneFromMessage(aMessage));
                }
            }

            formattedMessages.add(new BorderPane());
            messagesView.setItems(FXCollections.observableArrayList(formattedMessages));
            messagesView.scrollTo(messages.length);
        }
    }

    private void init() {
        chatWindowStage.initModality(Modality.WINDOW_MODAL);
        chatWindowStage.initOwner(primaryStage);

        chatWindowStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                chatWindowStage.hide();
            }
        });

        chatWindowStage.setTitle("you are chatting with " + toName);

        Rectangle2D r2d = Screen.getPrimary().getBounds();

        Random random = new Random(System.currentTimeMillis());

        int randomAdd = random.nextInt() % 50;
        double centerX = ((r2d.getWidth() - STAGE_W) / 2) + randomAdd;
        double centerY = ((r2d.getHeight() - STAGE_H) / 2) + randomAdd;                
        
        chatWindowStage.setX(centerX);
        chatWindowStage.setY(centerY);

        BorderPane mainPane = new BorderPane();

        sendFileButton = new Button("Send File");
        copySelectedButton = new Button("Copy Selected");

        GridPane topButtonsPane = new GridPane();
        topButtonsPane.add(sendFileButton, 0, 0);
        topButtonsPane.add(copySelectedButton, 1, 0);
        topButtonsPane.setHgap(10);
        ActionEventHandler actionEventHandler = new ActionEventHandler();
        sendFileButton.setOnAction(actionEventHandler);
        copySelectedButton.setOnAction(actionEventHandler);

        mainPane.setTop(topButtonsPane);

        messagesView = new ListView<BorderPane>();

        mainPane.setCenter(messagesView);

        messageToSendArea = new TextArea();

        messageToSendArea.setPrefHeight(100.0);
        messageToSendArea.setWrapText(true);
        messageToSendArea.requestFocus();
        messageToSendArea.setMaxHeight(Control.USE_PREF_SIZE);
        messageToSendArea.setOnKeyPressed(new KeyEventHandler());

        mainPane.setBottom(messageToSendArea);

        Scene scene = new Scene(mainPane, STAGE_W, STAGE_H);
        scene.getStylesheets().add(LoginPane.class.getResource("/res/control.css").toExternalForm());
        chatWindowStage.setScene(scene);
    }

    private BorderPane constructBorderPaneFromMessage(Message nv) {
        double currentWidth = chatWindowStage.getWidth();
        BorderPane p = new BorderPane();
        StackPane headerPane = new StackPane();
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(2.0f);
        innerShadow.setOffsetY(2.0f);
        headerPane.setEffect(innerShadow);
        if (nv.getFrom().equals(me)) {
            headerPane.getChildren().add(new Rectangle(currentWidth - 20, 25.0, Color.web("#544504")));
        } else {
            headerPane.getChildren().add(new Rectangle(currentWidth - 20, 25.0, Color.web("#A78732")));
        }

        BorderPane headerContentPane = new BorderPane();

        Text from = new Text("        " + nv.getFrom());
        from.getStyleClass().add("my-text");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Text date = new Text(format.format(nv.getTime()) + "        ");
        date.getStyleClass().add("my-text");

        headerContentPane.setLeft(from);
        headerContentPane.setRight(date);

        headerPane.getChildren().add(headerContentPane);


        Text text = new Text(nv.getMsg());
        
        text.setWrappingWidth(chatWindowStage.getWidth() - 20);

        p.setTop(headerPane);
        p.setCenter(text);

        return p;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    private class KeyEventHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent keyEvent) {
            try {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    Message message = new Message();
                    message.setFrom(me);
                    message.setTo(toName);
                    message.setMsg(messageToSendArea.getText());
                    message.setTime(new Date());

                    int len = 0;

                    Message[] newMessages = new Message[1];

                    if (Messages.list.get() != null) {
                        len = Messages.list.get().length;
                        newMessages = Arrays.copyOf(Messages.list.get(), len + 1);
                    }

                    newMessages[len] = message;

                    Messages.list.set(newMessages);

                    Messenger.sendMessage(message, client.clientSocket);
                    messageToSendArea.clear();

                } else if (messageToSendArea.getText() != null && messageToSendArea.getText().charAt(0) == '\n') {
                    messageToSendArea.setText(messageToSendArea.getText().substring(1));
                }
            } catch (java.lang.StringIndexOutOfBoundsException si) {
            }
        }
    }

    private class ActionEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            Button b = (Button) actionEvent.getSource();

            if (b.equals(sendFileButton)) {
                FileChooser fileChooser = new FileChooser();
                File f = fileChooser.showOpenDialog(primaryStage);
                Messenger.sendFile(f, client.clientSocket, toName);
            } else if (b.equals(copySelectedButton)) {
                Clipboard cp = Clipboard.getSystemClipboard();
                HashMap<DataFormat, Object> data = new HashMap<DataFormat, Object>();
                DataFormat f = DataFormat.PLAIN_TEXT;

                BorderPane si = messagesView.getSelectionModel().getSelectedItem();

                Text t = (Text) si.getChildren().get(1);

                data.put(f, t.getText());
                cp.setContent(data);

            }
        }
    }

    public void setClient(Client client) {
        this.client = client;        
    }
}
