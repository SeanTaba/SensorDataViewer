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
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        primaryStage.setTitle("Sensor Data Viewer");
        Scene scene = new Scene(root);
        scene.setUserData(controller);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception
    {
        if (controller.getPort() != null) {
            if (controller.getPort().isOpen()) {
                controller.getPort().closePort();
            }
        }
        super.stop();
    }

    public Controller getController() {
        return controller;
    }
}
