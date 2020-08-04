package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

public class TerminalTask extends Task<Void>
{
    private final SerialPort port;
    private final TextArea textArea;

    public TerminalTask(SerialPort port, TextArea textArea)
    {
        this.port = port;
        this.textArea = textArea;
    }

    @Override
    protected Void call()
    {
        System.out.println("TerminalTask was started");
        boolean run = true;
        while (run)
        {
            try
            {
                if (port.bytesAvailable() > 0)
                {
                    byte[] data = new byte[port.bytesAvailable()];
                    port.readBytes(data, port.bytesAvailable());
                    String message = new String(data);
                    Platform.runLater(() -> textArea.appendText(message));
                }
                Thread.sleep(10);
            } catch (InterruptedException ignored)
            {
                run = false;
            }
        }
        System.out.println("TerminalTask was stopped");
        return null;
    }


}
