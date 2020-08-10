package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialPortReceiverTask extends Task<Void>
{
    private final SerialPort port;
    private final ObservableList<DataModel> list;
    private final DateFormat dateFormat;
    private final Date date;

    public SerialPortReceiverTask(SerialPort port, ObservableList<DataModel> list)
    {
        this.port = port;
        this.list = list;
        this.dateFormat = new SimpleDateFormat("MM_dd_yy-HH_mm_ss");
        this.date = new Date();
    }

    @Override
    protected Void call()
    {
        System.out.println("SerialPortReceiverTask was started");
        boolean run = true;

        Path path = FileSystems.getDefault().getPath("src", "com", "seantaba", "data", dateFormat.format(date) + ".dat");

        try (FileOutputStream fileOutputStream = new FileOutputStream(path.toAbsolutePath().toString()); BufferedOutputStream outputStream =
                new BufferedOutputStream(fileOutputStream); DataInputStream InputStream = new DataInputStream(port.getInputStream()))
        {
            while (run)
            {
                if (!port.isOpen())
                {
                    run = false;
                }
                try
                {
                    if (InputStream.available() > 29)
                    {
                        boolean found = false;
                        int counter = 0;
                        while (!found)
                        {
                            byte header = InputStream.readByte();
                            if (header == -1)
                            {
                                counter++;
                            } else
                            {
                                counter = 0;
                            }
                            if (counter == 4) found = true;
                        }
                        int time = InputStream.readInt();
                        int sensor1 = InputStream.readInt();
                        int sensor2 = InputStream.readInt();
                        int sensor3 = InputStream.readInt();
                        int sensor4 = InputStream.readInt();

                        Platform.runLater(() ->
                        {
                            DataModel dataModel = new DataModel(new SimpleStringProperty(Long.toString(time)),
                                    new SimpleStringProperty(Integer.toString(sensor1)), new SimpleStringProperty(Integer.toString(sensor2)),
                                    new SimpleStringProperty(Integer.toString(sensor3)), new SimpleStringProperty(Integer.toString(sensor4)));
                            list.add(dataModel);
                            try
                            {
                                outputStream.write(time);
                                outputStream.write(sensor1);
                                outputStream.write(sensor2);
                                outputStream.write(sensor3);
                                outputStream.write(sensor4);
                                outputStream.flush();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        });
                    }
                    Thread.sleep(10);
                } catch (InterruptedException ignored)
                {
                    run = false;
                }
            }
        } catch (IOException e)
        {
            System.out.println("IO Error in SerialPortReceiverTask. " + e.getMessage());
        }

        System.out.println("SerialPortReceiverTask was stopped");
        return null;
    }

}
