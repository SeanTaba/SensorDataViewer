package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Controller {

    private SerialPort port;

    @FXML
    private MenuItem menuExit;
    @FXML
    private MenuItem menuConnect;
    @FXML
    private Button testButton;

    @FXML
    public void menuActionHandler(ActionEvent event) throws Exception {
        if (event.getSource().equals(menuExit))
        {
            Platform.exit();
        } else if (event.getSource().equals(menuConnect))
        {
            launchConnectWindow();
        }
    }

    public void launchConnectWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("connection.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Connect to a Port");
        Scene scene = new Scene(root);
        scene.setUserData(testButton.getScene().getUserData());
        primaryStage.setScene(scene);
        primaryStage.initOwner(testButton.getScene().getWindow());
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.showAndWait();

    }

    public SerialPort getPort() {
        return port;
    }
}
