package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
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
    private final ObservableList<DataModel> data;
    private final DateFormat dateFormat;
    private final Date date;
    private static final int WINDOW_WIDTH = 20;


    public SerialPortReceiverTask(SerialPort port, ObservableList<DataModel> data)
    {
        this.port = port;
        this.data = data;
        this.dateFormat = new SimpleDateFormat("(MM_dd_yy)-(HH_mm_ss)");
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
                    continue;
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
                            DataModel dataModel = new DataModel(new SimpleLongProperty(time),
                                    new SimpleIntegerProperty(sensor1), new SimpleIntegerProperty(sensor2),
                                    new SimpleIntegerProperty(sensor3), new SimpleIntegerProperty(sensor4));
                            data.add(dataModel);

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
                                System.out.println(e.getLocalizedMessage());
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

    private void updateSeries(XYChart.Series<String, Number> series, int time, int sensor)
    {
        series.getData().add(new XYChart.Data<>(String.valueOf(time), sensor));
        if (series.getData().size() > WINDOW_WIDTH) series.getData().remove(0);
    }

    private void updateAverageSeries(XYChart.Series<String, Number> averageSeries, int time, XYChart.Series<String, Number> series)
    {
        int seriesSize = series.getData().size();
        double average = 0;
        for (int i = seriesSize - 1; i > seriesSize - 10; i--)
        {
            if (i >= 0) average += ((int) series.getData().get(i).getYValue()) / 10.0;
        }
        averageSeries.getData().add(new XYChart.Data<>(String.valueOf(time), average));
        if (averageSeries.getData().size() > WINDOW_WIDTH) averageSeries.getData().remove(0);
    }
}
