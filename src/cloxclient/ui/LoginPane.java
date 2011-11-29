/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cloxclient.ui;

import cloxclient.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author trinisoftinc
 */
public class LoginPane implements EventHandler<ActionEvent> {

    TextField usernameField, passwordField;
    Button loginButton, cancelButton;
    Stage primaryStage;
    Stage loginStage = new Stage();

    public LoginPane(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        init();
    }

    public void show() {
        loginStage.show();
    }

    private void init() {
        loginStage.initModality(Modality.WINDOW_MODAL);
        loginStage.initOwner(primaryStage);

        BorderPane mainPane = new BorderPane();
        GridPane centerPane = new GridPane();
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.setPadding(new Insets(5, 5, 5, 5));

        centerPane.add(new Label("Username"), 1, 1);
        centerPane.add((usernameField = new TextField("")), 2, 1);

        centerPane.add(new Label("Password"), 1, 2);
        centerPane.add((passwordField = new TextField("")), 2, 2);

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().add((loginButton = new Button("Login")));
        buttonsBox.getChildren().add(cancelButton = new Button("Cancel"));
        centerPane.add(buttonsBox, 2, 3);
        mainPane.setCenter(centerPane);

        Scene scene = new Scene(mainPane, 280, 150);

        scene.getStylesheets().add(LoginPane.class.getResource("/res/control.css").toExternalForm());

        loginStage.setScene(scene);

        loginButton.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent t) {
        Button b = (Button) t.getSource();
        if (b.equals(loginButton)) {
            final Client client = new Client("segun", null);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    client.start();
                }
            });            
            
            ChatList chatList = new ChatList(primaryStage);
            chatList.show();
        }
    }
}