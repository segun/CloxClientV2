/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cloxclient.ui;

import javafx.stage.Stage;

/**
 *
 * @author trinisoftinc
 */
public class ClientList {

    Stage primaryStage;
    Stage clientListStage;
    
    
    public ClientList(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        init();
    }
    
    public void show() {
        clientListStage.show();
    }
    
    public void hide() {
        clientListStage.hide();
    }
    
    private void init() {
        
    }
}
