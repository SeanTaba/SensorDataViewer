package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TerminalController {
    @FXML
    private TextArea textArea;
    private Thread terminalTaskThread;

    public void runTerminalTask()
    {
        terminalTaskThread = new Thread(new TerminalTask((SerialPort) textArea.getScene().getUserData(), textArea));
        terminalTaskThread.start();
    }
    public void stopTerminalTask()
    {
        terminalTaskThread.interrupt();
    }
}
