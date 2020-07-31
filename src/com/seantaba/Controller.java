package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Controller {

    private SerialPort port;

    @FXML
    private MenuItem menuExit;
    @FXML
    private MenuItem menuConnect;
    @FXML
    private MenuItem menuDisconnect;
    @FXML
    private Button testButton;
    @FXML
    private Label statusLabel;

    @FXML
    public void menuActionHandler(ActionEvent event) throws Exception {
        if (event.getSource().equals(menuExit))
        {
            Platform.exit();
        } else if (event.getSource().equals(menuConnect))
        {
            launchConnectWindow();
        } else if (event.getSource().equals(menuDisconnect))
        {
            if (port.isOpen())
            {
                if (port.closePort())
                {
                    statusLabel.setText("Not Connected!");
                    menuConnect.setDisable(false);
                    menuDisconnect.setDisable(true);
                } else
                {
                    System.out.println("Unable to close the port!");
                }
            }
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
        if (connect())
        {
            statusLabel.setText("Connected!");
            menuConnect.setDisable(true);
            menuDisconnect.setDisable(false);
        } else
        {
            statusLabel.setText("Not Connected!");
            menuConnect.setDisable(false);
            menuDisconnect.setDisable(true);
        }
    }

    public boolean connect()
    {
        if (port != null)
        {
            return port.openPort();
        }
        return false;
    }

    public SerialPort getPort() {
        return port;
    }

    public void setPort(SerialPort port)
    {
        this.port = port;
    }
}
