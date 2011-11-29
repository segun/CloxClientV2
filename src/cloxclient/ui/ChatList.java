/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cloxclient.ui;

import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author trinisoftinc
 */
public class ChatList extends Group {

    Stage chatListStage = new Stage(),
            primaryStage;
    public static ObjectProperty<Object[]> clients = new SimpleObjectProperty<Object[]>();

    public ChatList(Stage primaryStage) {
        this.primaryStage = primaryStage;
        init();
    }

    public void show() {
        chatListStage.show();
    }

    private void init() {
        chatListStage.initModality(Modality.WINDOW_MODAL);
        chatListStage.initOwner(primaryStage);

        final VBox mainPane = new VBox(10);

        Scene scene = new Scene(mainPane, 280, 150);

        scene.getStylesheets().add(LoginPane.class.getResource("/res/control.css").toExternalForm());

        chatListStage.setScene(scene);

        //chatListStage.setOnAction(this);        
        clients.addListener(new ChangeListener<Object[]>() {

            @Override
            public void changed(ObservableValue<? extends Object[]> observable, Object[] oldValue, final Object[] newValue) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        if (mainPane.getChildren().size() > 0) {
                            Node remove = mainPane.getChildren().remove(0);
                        }

                        ListView listView = new ListView(FXCollections.observableArrayList(Arrays.asList(newValue)));
                        mainPane.getChildren().add(listView);
                    }
                });
            }
        });

    }
}
