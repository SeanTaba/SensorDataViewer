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
    private MenuItem menuTerminal;
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
                    menuTerminal.setDisable(true);
                } else
                {
                    System.out.println("Unable to close the port!");
                }
            }
        } else if (event.getSource().equals(menuTerminal))
        {
            launchTerminalWindow();
        }
    }

    public void launchConnectWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("connection.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Connect to a Port");
        Scene scene = new Scene(root);
        scene.setUserData(testButton.getScene().getUserData());
        stage.setScene(scene);
        stage.initOwner(testButton.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        if (connect())
        {
            statusLabel.setText("Connected!");
            menuConnect.setDisable(true);
            menuDisconnect.setDisable(false);
            menuTerminal.setDisable(false);
        } else
        {
            statusLabel.setText("Not Connected!");
            menuConnect.setDisable(false);
            menuDisconnect.setDisable(true);
            menuTerminal.setDisable(true);
        }
    }
    public void launchTerminalWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("terminal.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Serial Monitor");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
