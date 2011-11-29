/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cloxclient;

import cloxclient.ui.LoginPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author trinisoftinc
 */
public class CloxClient extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Clox Login");
        Scene scene = null;

        Client.host = "unotifier.com";
        Client.port = 1981;
        
        new LoginPane(primaryStage).show();

        //primaryStage.setScene(scene);
        //primaryStage.show();
    }
}
