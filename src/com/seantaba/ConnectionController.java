package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class ConnectionController
{

    private SerialPort port;
    private SerialPort[] availablePorts = SerialPort.getCommPorts();

    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonConnect;
    @FXML
    private ListView<SerialPort> listView;

    public void initialize()
    {
        if (availablePorts.length > 0) buttonConnect.setDisable(false);
        ObservableList<SerialPort> ports = FXCollections.observableArrayList(availablePorts);
        listView.setItems(ports);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectFirst();
        listView.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(SerialPort item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setText(null);
                } else
                {
                    setText(item.getDescriptivePortName());
                }
            }
        });

    }


    public void buttonHandler(ActionEvent event)
    {
        if (event.getSource().equals(buttonCancel))
        {
            Scene scene = buttonCancel.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        } else if (event.getSource().equals(buttonConnect))
        {
            port = listView.getSelectionModel().getSelectedItem();
            if (port != null)
            {
                MainController mainController = (MainController) buttonConnect.getScene().getUserData();
                mainController.setPort(port);
            }
            ((Stage) buttonConnect.getScene().getWindow()).close();
        }
    }
}
