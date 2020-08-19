package com.seantaba;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        primaryStage.setTitle("Sensor Data Viewer");
        primaryStage.setOnCloseRequest(event -> {
            try
            {
                controller.stop();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Platform.exit();
        });
        Scene scene = new Scene(root);
        scene.setUserData(controller);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public MainController getController() {
        return controller;
    }
}
