package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class TerminalController {
    @FXML
    private TextArea textArea;
    private Thread terminalTaskThread;
    @FXML
    private Button sendButton;
    @FXML
    private ComboBox<String> sendComboBox;
    @FXML
    private ComboBox<String> optionsComboBox;
    @FXML
    private CheckBox ASCheckBox;

    private final ObservableList<String> sendComboBoxList = FXCollections.observableArrayList();
    private SerialPort port;

    public void initialize() {
        sendComboBox.setItems(sendComboBoxList);
    }

    public void buttonActionHandler(ActionEvent event) {
        if (!sendComboBox.getSelectionModel().getSelectedItem().equals("")) {
            String str = sendComboBox.getValue();
            if (!sendComboBoxList.contains(str)) sendComboBoxList.add(str);
            String postFix = switch (optionsComboBox.getValue()) {
                case "NL" -> "\n";
                case "CR" -> "\r";
                case "NL and CR" -> "\n\r";
                default -> "";
            };
            str += postFix;
            port.writeBytes(str.getBytes(), str.length());
        }
    }

    public void runTerminalTask()
    {
        terminalTaskThread = new Thread(new TerminalTask(port, textArea));
        terminalTaskThread.start();
    }
    public void stopTerminalTask()
    {
        terminalTaskThread.interrupt();
    }

    public void setPort(SerialPort port)
    {
        this.port = port;
    }
}
