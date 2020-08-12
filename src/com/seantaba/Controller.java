package com.seantaba;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    private SerialPort port;
    private final ObservableList<DataModel> data = FXCollections.observableArrayList();
    private SerialPortReceiverTask receiverTask;
    private final XYChart.Series<String,Number> series1 = new XYChart.Series<>();
    private final XYChart.Series<String,Number> series2 = new XYChart.Series<>();
    private final XYChart.Series<String,Number> series3 = new XYChart.Series<>();
    private final XYChart.Series<String,Number> series4 = new XYChart.Series<>();

    @FXML
    private MenuItem menuExit;
    @FXML
    private MenuItem menuConnect;
    @FXML
    private MenuItem menuDisconnect;
    @FXML
    private MenuItem menuTerminal;
    @FXML
    private Button testButton;
    @FXML
    private Label statusLabel;
    @FXML
    private MenuItem menuRawDataLineChart;

    @FXML
    public void menuActionHandler(ActionEvent event) throws Exception {
        if (event.getSource().equals(menuExit))
        {
            Platform.exit();
        } else if (event.getSource().equals(menuConnect))
        {
            launchConnectWindow();
        } else if (event.getSource().equals(menuDisconnect))
        {
            if (port.isOpen())
            {
                if (port.closePort())
                {
                    statusLabel.setText("Not Connected!");
                    menuConnect.setDisable(false);
                    menuDisconnect.setDisable(true);
                    menuTerminal.setDisable(true);
                } else
                {
                    System.out.println("Unable to close the port!");
                }
            }
        } else if (event.getSource().equals(menuTerminal))
        {
            launchTerminalWindow();
        } else if (event.getSource().equals(menuRawDataLineChart))
        {
            launchRawDataLineChart();
        }
    }

    public void launchConnectWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("connection.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Connect to a Port");
        Scene scene = new Scene(root);
        scene.setUserData(testButton.getScene().getUserData());
        stage.setScene(scene);
        stage.initOwner(testButton.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        if (connect())
        {
            statusLabel.setText("Connected!");
            menuConnect.setDisable(true);
            menuDisconnect.setDisable(false);
            menuTerminal.setDisable(false);
        } else
        {
            statusLabel.setText("Not Connected!");
            menuConnect.setDisable(false);
            menuDisconnect.setDisable(true);
            menuTerminal.setDisable(true);
        }
    }
    public void launchTerminalWindow() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("terminal.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Serial Monitor");
        TerminalController controller = fxmlLoader.getController();
        controller.setPort(port);
        controller.setReceived(data);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(value ->
                controller.turnAutoScrollOff());
        stage.show();
    }
    public void launchRawDataLineChart() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rawdatalinechart.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Raw Data Line Chart");
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time (ms)");
        xAxis.setAnimated(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAnimated(false);
        LineChart<String,Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Raw Sensor Data");
        lineChart.setAnimated(false);

        series1.setName("Sensor 1");
        series2.setName("Sensor 2");
        series3.setName("Sensor 3");
        series4.setName("Sensor 4");

        lineChart.getData().add(series1);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
        lineChart.getData().add(series4);

        Scene scene = new Scene(lineChart,1000,600);
        stage.setScene(scene);
        stage.show();
    }

    public boolean connect()
    {
        if (port != null)
        {
            if (port.openPort())
            {
                receiverTask = new SerialPortReceiverTask(port, data,series1,series2,series3,series4);
                new Thread(receiverTask).start();
                return true;
            }
        }
        return false;
    }

    public SerialPort getPort() {
        return port;
    }

    public void setPort(SerialPort port)
    {
        this.port = port;
    }
}
