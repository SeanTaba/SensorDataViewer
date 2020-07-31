package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ConnectionController {

    private SerialPort port;
    private SerialPort[] availablePorts = SerialPort.getCommPorts();

    @FXML
    private Button buttonCancel;
    @FXML
    private TextArea textArea;

    public void initialize()
    {
        //port = ((Controller) buttonCancel.getScene().getUserData()).getPort();
        if (availablePorts.length == 0)
        {
            textArea.setText("No Available COM Ports");
        } else
        {
            for (SerialPort port :
                    availablePorts) {
                textArea.appendText(port.getDescriptivePortName() + "\n");
            }
        }
    }

    public void buttonHandler(ActionEvent event)
    {
        if (event.getSource().equals(buttonCancel))
        {
            Scene scene = buttonCancel.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        }
    }
}
