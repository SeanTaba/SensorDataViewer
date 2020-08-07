package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class TerminalTask extends Task<Void> {
    private final SerialPort port;
    private final ObservableList<DataModel> list;

    public TerminalTask(SerialPort port, ObservableList<DataModel> list) {
        this.port = port;
        this.list = list;
    }

    @Override
    protected Void call() {
        System.out.println("TerminalTask was started");
        boolean run = true;
        while (run) {
            try {
                if (!port.isOpen()) {
                    run = false;
                }
                if (port.bytesAvailable() > 0) {
                    byte[] data = new byte[port.bytesAvailable()];
                    port.readBytes(data, port.bytesAvailable());
                    String received = new String(data);
                    String[] messages = received.split("\r\n");
                    for (String message : messages) {
                        String[] contents = message.split(",");
                        Platform.runLater(() -> {
                            DataModel dataModel = new DataModel(new SimpleStringProperty(contents[0]), new SimpleStringProperty(contents[1]),
                                    new SimpleStringProperty(contents[2]), new SimpleStringProperty(contents[3]), new SimpleStringProperty(contents[4]));
                            list.add(dataModel);
                        });
                    }

                }
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                run = false;
            }
        }
        System.out.println("TerminalTask was stopped");
        return null;
    }

}
