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
    private final XYChart.Series<String, Number> series1;
    private final XYChart.Series<String, Number> series2;
    private final XYChart.Series<String, Number> series3;
    private final XYChart.Series<String, Number> series4;
    private final XYChart.Series<String, Number> averageSeries1;
    private final XYChart.Series<String, Number> averageSeries2;
    private final XYChart.Series<String, Number> averageSeries3;
    private final XYChart.Series<String, Number> averageSeries4;
    private static final int WINDOW_WIDTH = 20;

    public SerialPortReceiverTask(SerialPort port, ObservableList<DataModel> list, XYChart.Series<String, Number> series1,
                                  XYChart.Series<String, Number> series2, XYChart.Series<String, Number> series3, XYChart.Series<String, Number> series4, XYChart.Series<String, Number> averageSeries1, XYChart.Series<String, Number> averageSeries2, XYChart.Series<String, Number> averageSeries3, XYChart.Series<String, Number> averageSeries4)
    {
        this.port = port;
        this.list = list;
        this.averageSeries1 = averageSeries1;
        this.averageSeries2 = averageSeries2;
        this.averageSeries3 = averageSeries3;
        this.averageSeries4 = averageSeries4;
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
                            DataModel dataModel = new DataModel(new SimpleStringProperty(Long.toString(time)),
                                    new SimpleStringProperty(Integer.toString(sensor1)), new SimpleStringProperty(Integer.toString(sensor2)),
                                    new SimpleStringProperty(Integer.toString(sensor3)), new SimpleStringProperty(Integer.toString(sensor4)));
                            list.add(dataModel);

                            updateSeries(series1, time, sensor1);
                            updateSeries(series2, time, sensor2);
                            updateSeries(series3, time, sensor3);
                            updateSeries(series4, time, sensor4);
                            updateAverageSeries(averageSeries1, time, series1);
                            updateAverageSeries(averageSeries2, time, series2);
                            updateAverageSeries(averageSeries3, time, series3);
                            updateAverageSeries(averageSeries4, time, series4);
//
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
