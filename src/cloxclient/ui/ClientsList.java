/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cloxclient.ui;

import cloxclient.Client;
import cloxclient.CloxClient;
import cloxclient.helpers.MessageDisparser;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author trinisoftinc
 */
public class ClientsList implements EventHandler<ActionEvent> {

    public static final int STAGE_W = 280;
    public static final int STAGE_H = 600;
    Stage chatListStage = new Stage(),
            primaryStage;
    LoginPane loginPane;
    public static ObjectProperty<Object[]> clients = new SimpleObjectProperty<Object[]>();
    String username;
    Client client;
    Button logoutButton;
    ListView<Object> clientsListView;
    MessageDisparser messageDisparser;

    public ClientsList(String username, Client client, LoginPane loginPane, MessageDisparser disparser) {
        this.primaryStage = CloxClient.primaryStage;
        this.username = username;
        this.client = client;
        this.loginPane = loginPane;
        this.messageDisparser = disparser;                
        init();
    }

    public void show() {
        chatListStage.show();
    }

    private void init() {
        chatListStage.initModality(Modality.WINDOW_MODAL);
        chatListStage.initOwner(primaryStage);

        chatListStage.setTitle("Online Contacts");
        
        chatListStage.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                client.disconnect();
                chatListStage.hide();
                loginPane.show();
            }
        });

        final VBox mainPane = new VBox(10);

        Label userNameLabel = new Label(username);
        logoutButton = new Button("Log out");

        logoutButton.setOnAction(this);
        HBox userPane = new HBox(20);

        userPane.getChildren().add(userNameLabel);
        userPane.getChildren().add(logoutButton);

        userPane.setTranslateX(20);
        userPane.setTranslateY(20);

        final VBox clientsPane = new VBox();
        clientsPane.setTranslateY(40);
        clientsPane.setPrefHeight(540);

        mainPane.getChildren().add(userPane);
        mainPane.getChildren().add(clientsPane);


        Rectangle2D r2d = Screen.getPrimary().getBounds();

        chatListStage.setResizable(false);
        chatListStage.setFullScreen(false);
        chatListStage.setX(r2d.getWidth() - (STAGE_W + 50));
        chatListStage.setY((r2d.getHeight() - STAGE_H) / 2);
        Scene scene = new Scene(mainPane, STAGE_W, STAGE_H);

        ScrollPane s = new ScrollPane();
        s.setContent(clientsPane);

        scene.getStylesheets().add(LoginPane.class.getResource("/res/control.css").toExternalForm());

        chatListStage.setScene(scene);

        clientsListView = new ListView<Object>();
        
        clientsPane.getChildren().add(clientsListView);
        
        clientsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (clientsListView.getSelectionModel().getSelectedItems().size() > 0) {
                        String toName = clientsListView.getSelectionModel().getSelectedItem().toString();
                        ChatWindow chatWindow = messageDisparser.getChats().get(toName);
                        if(chatWindow == null) {
                            chatWindow = new ChatWindow(primaryStage, client, null, toName, username);
                            messageDisparser.getChats().put(toName, chatWindow);
                        }
                        
                        chatWindow.show();
                        //new ChatWindow(primaryStage, username, , client, username).show();
                    }
                }
            }
        });

        clients.addListener(new ChangeListener<Object[]>() {

            @Override
            public void changed(ObservableValue<? extends Object[]> observable, final Object[] oldValue, final Object[] newValue) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        Object nv[] = newValue;
                        clientsListView.setItems(FXCollections.observableArrayList(Arrays.asList(nv)));
                    }
                });
            }
        });
    }

    @Override
    public void handle(ActionEvent event) {
        Button b = (Button) event.getSource();
        if (b.equals(logoutButton)) {
            client.disconnect();
            chatListStage.hide();
            loginPane.show();
        }
    }
}
