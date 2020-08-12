package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
    private final XYChart.Series<String,Number> series1;
    private final XYChart.Series<String,Number> series2;
    private final XYChart.Series<String,Number> series3;
    private final XYChart.Series<String,Number> series4;
    private static final int WINDOW_WIDTH = 20;

    public SerialPortReceiverTask(SerialPort port, ObservableList<DataModel> list, XYChart.Series<String,Number> series1,
                                  XYChart.Series<String,Number> series2, XYChart.Series<String,Number> series3, XYChart.Series<String,Number> series4)
    {
        this.port = port;
        this.list = list;
        this.dateFormat = new SimpleDateFormat("(MM_dd_yy)-(HH_mm_ss)");
        this.date = new Date();
        this.series1 = series1;
        this.series2 = series2;
        this.series3 = series3;
        this.series4 = series4;
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

                            series1.getData().add(new XYChart.Data<>(String.valueOf(time),sensor1));
                            if (series1.getData().size() > WINDOW_WIDTH) series1.getData().remove(0);
                            series2.getData().add(new XYChart.Data<>(String.valueOf(time),sensor2));
                            if (series2.getData().size() > WINDOW_WIDTH) series2.getData().remove(0);
                            series3.getData().add(new XYChart.Data<>(String.valueOf(time),sensor3));
                            if (series3.getData().size() > WINDOW_WIDTH) series3.getData().remove(0);
                            series4.getData().add(new XYChart.Data<>(String.valueOf(time),sensor4));
                            if (series4.getData().size() > WINDOW_WIDTH) series4.getData().remove(0);


                            try
                            {
                                ByteBuffer buffer = ByteBuffer.allocate(4);
                                outputStream.write(buffer.putInt(time).array());
                                buffer.flip();
                                outputStream.write(buffer.putInt(sensor1).array());
                                buffer.flip();
                                outputStream.write(buffer.putInt(sensor2).array());
                                buffer.flip();
                                outputStream.write(buffer.putInt(sensor3).array());
                                buffer.flip();
                                outputStream.write(buffer.putInt(sensor4).array());
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
