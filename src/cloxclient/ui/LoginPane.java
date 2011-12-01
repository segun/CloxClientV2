/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cloxclient.ui;

import cloxclient.Client;
import cloxclient.CloxClient;
import cloxclient.helpers.MessageDisparser;
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
    MessageDisparser messageDisparser;
    ClientsList clientsList;

    public LoginPane() {
        this.primaryStage = CloxClient.primaryStage;
        init();
    }

    public void show() {
        loginStage.show();
    }

    private void init() {
        loginStage.initModality(Modality.WINDOW_MODAL);
        loginStage.initOwner(primaryStage);

        loginStage.setTitle("Please Login");

        BorderPane mainPane = new BorderPane();
        GridPane centerPane = new GridPane();
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.setPadding(new Insets(5, 5, 5, 5));

        centerPane.add(new Label("Username"), 1, 1);
        centerPane.add((usernameField = new TextField("")), 2, 1);

        usernameField.setOnAction(this);

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
        cancelButton.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent t) {

        Button bt = null;
        TextField tf = null;
        if (t.getSource() instanceof Button) {
            bt = (Button) t.getSource();
        } else {
            tf = (TextField) t.getSource();
        }
        final LoginPane instance = this;
        if ((bt != null && bt.equals(loginButton)) || tf != null) {
            final Client client = new Client(usernameField.getText(), primaryStage);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    client.start();
                    if (messageDisparser == null) {
                        messageDisparser = new MessageDisparser(primaryStage, client, usernameField.getText());
                    } else {
                        messageDisparser.setClient(client);
                        messageDisparser.setMe(usernameField.getText());
                    }

                    if (clientsList == null) {
                        clientsList = new ClientsList(usernameField.getText(), client, instance, messageDisparser);
                    } else {
                        clientsList.client = client; 
                        clientsList.username = usernameField.getText();
                    }
                    
                    clientsList.show();
                    loginStage.hide();
                }
            });
        } else if (bt.equals(cancelButton)) {
            System.exit(0);
        }
    }
}