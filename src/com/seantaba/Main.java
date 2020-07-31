package com.seantaba;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        controller = fxmlLoader.getController();
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Sensor Data Viewer");
        Scene scene = new Scene(root);
        scene.setUserData(controller);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Controller getController() {
        return controller;
    }
}
