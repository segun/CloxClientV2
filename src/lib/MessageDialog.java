package lib;

import cloxclient.ui.LoginPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageDialog {

    public static Stage stage;

    public static void showMessageDialog(final Stage primaryStage, final String text) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                stage = new Stage();

                Label label = new Label(text);
                label.setPrefSize(350, 100);
                label.setAlignment(Pos.BASELINE_CENTER);
                label.setWrapText(true);
                label.autosize();

                Button button = new Button("OK");
                button.setAlignment(Pos.BASELINE_RIGHT);
                button.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        stage.hide();
                    }
                });

                AnchorPane anchorpane = new AnchorPane();
                AnchorPane.setTopAnchor(label, 10.0);
                AnchorPane.setBottomAnchor(button, 10.0);
                AnchorPane.setLeftAnchor(label, 10.0);
                AnchorPane.setRightAnchor(button, 10.0);

                anchorpane.getChildren().addAll(label, button);
                anchorpane.setPrefSize(400, 150);

                Scene scene = new Scene(anchorpane);
                scene.getStylesheets().add(LoginPane.class.getResource("/res/control.css").toExternalForm());

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(primaryStage);
                stage.setScene(scene);
                stage.show();
            }
        });
    }

    public static void showConfirmDialog(final Stage primaryStage, final String text, final EventHandler<ActionEvent> eventHandler) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                stage = new Stage();

                Label label = new Label(text);
                label.setPrefSize(350, 100);
                label.setAlignment(Pos.BASELINE_CENTER);
                label.setWrapText(true);
                label.autosize();

                Button yesButton = new Button("Yes");
                yesButton.setAlignment(Pos.BASELINE_RIGHT);
                yesButton.setOnAction(eventHandler);

                Button noButton = new Button("No");
                noButton.setAlignment(Pos.BASELINE_RIGHT);
                noButton.setOnAction(eventHandler);

                AnchorPane anchorpane = new AnchorPane();
                AnchorPane.setTopAnchor(label, 10.0);
                AnchorPane.setLeftAnchor(label, 10.0);

                AnchorPane.setBottomAnchor(yesButton, 10.0);
                AnchorPane.setRightAnchor(yesButton, 60.0);

                AnchorPane.setBottomAnchor(noButton, 10.0);
                AnchorPane.setRightAnchor(noButton, 10.0);

                anchorpane.getChildren().addAll(label, yesButton, noButton);
                anchorpane.setPrefSize(400, 150);

                Scene scene = new Scene(anchorpane);
                scene.getStylesheets().add(LoginPane.class.getResource("/res/control.css").toExternalForm());

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(primaryStage);
                stage.setScene(scene);
                stage.show();
            }
        });
    }
}