package com.seantaba;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;

public class RawDataLineChartController
{
    private XYChart.Series<String,Number> series1;
    private XYChart.Series<String,Number> series2;
    private XYChart.Series<String,Number> series3;
    private XYChart.Series<String,Number> series4;
    private XYChart.Series<String, Number> averageSeries1;
    private XYChart.Series<String, Number> averageSeries2;
    private XYChart.Series<String, Number> averageSeries3;
    private XYChart.Series<String, Number> averageSeries4;

    @FXML
    private LineChart<String,Number> lineChart;
    @FXML
    private CategoryAxis lineChartXAxis;
    @FXML
    private NumberAxis lineChartYAxis;
    @FXML
    private CheckBox sensor1CheckBox;
    @FXML
    private CheckBox sensor2CheckBox;
    @FXML
    private CheckBox sensor3CheckBox;
    @FXML
    private CheckBox sensor4CheckBox;
    @FXML
    private CheckBox sensor1AverageCheckBox;
    @FXML
    private CheckBox sensor2AverageCheckBox;
    @FXML
    private CheckBox sensor3AverageCheckBox;
    @FXML
    private CheckBox sensor4AverageCheckBox;

    public void initialize()
    {
        lineChartXAxis.setLabel("Time (ms)");
        lineChartXAxis.setAnimated(false);
        lineChartYAxis.setLabel("Value");
        lineChartYAxis.setAnimated(false);

        lineChart.setTitle("Raw Sensor Data");
        lineChart.setAnimated(false);
    }

    public void setup(XYChart.Series<String,Number> series1, XYChart.Series<String,Number> series2,
                      XYChart.Series<String,Number> series3, XYChart.Series<String,Number> series4,
                      XYChart.Series<String,Number> averageSeries1, XYChart.Series<String,Number> averageSeries2,
                      XYChart.Series<String,Number> averageSeries3, XYChart.Series<String,Number> averageSeries4)
    {
        this.series1 = series1;
        this.series2 = series2;
        this.series3 = series3;
        this.series4 = series4;
        this.averageSeries1 = averageSeries1;
        this.averageSeries2 = averageSeries2;
        this.averageSeries3 = averageSeries3;
        this.averageSeries4 = averageSeries4;

        series1.setName("Sensor 1");
        series2.setName("Sensor 2");
        series3.setName("Sensor 3");
        series4.setName("Sensor 4");
        averageSeries1.setName("Average Sensor 1");
        averageSeries2.setName("Average Sensor 2");
        averageSeries3.setName("Average Sensor 3");
        averageSeries4.setName("Average Sensor 4");

        lineChart.getData().add(series1);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
        lineChart.getData().add(series4);
        lineChart.getData().add(averageSeries1);
        lineChart.getData().add(averageSeries2);
        lineChart.getData().add(averageSeries3);
        lineChart.getData().add(averageSeries4);
    }

    public void sensorChoiceHandler(ActionEvent event)
    {
        if (event.getSource().equals(sensor1CheckBox))
        {
            if (sensor1CheckBox.isSelected())
            {
                lineChart.getData().add(series1);
            } else
            {
                lineChart.getData().remove(series1);
            }
        } else if (event.getSource().equals(sensor2CheckBox))
        {
            if (sensor2CheckBox.isSelected())
            {
                lineChart.getData().add(series2);
            } else
            {
                lineChart.getData().remove(series2);
            }
        } else if (event.getSource().equals(sensor3CheckBox))
        {
            if (sensor3CheckBox.isSelected())
            {
                lineChart.getData().add(series3);
            } else
            {
                lineChart.getData().remove(series3);
            }
        } else if (event.getSource().equals(sensor4CheckBox))
        {
            if (sensor4CheckBox.isSelected())
            {
                lineChart.getData().add(series4);
            } else
            {
                lineChart.getData().remove(series4);
            }
        } else if (event.getSource().equals(sensor1AverageCheckBox))
        {
            if (sensor1AverageCheckBox.isSelected())
            {
                lineChart.getData().add(averageSeries1);
            } else
            {
                lineChart.getData().remove(averageSeries1);
            }
        } else if (event.getSource().equals(sensor2AverageCheckBox))
        {
            if (sensor2AverageCheckBox.isSelected())
            {
                lineChart.getData().add(averageSeries2);
            } else
            {
                lineChart.getData().remove(averageSeries2);
            }
        } else if (event.getSource().equals(sensor3AverageCheckBox))
        {
            if (sensor3AverageCheckBox.isSelected())
            {
                lineChart.getData().add(averageSeries3);
            } else
            {
                lineChart.getData().remove(averageSeries3);
            }
        } else if (event.getSource().equals(sensor4AverageCheckBox))
        {
            if (sensor4AverageCheckBox.isSelected())
            {
                lineChart.getData().add(averageSeries4);
            } else
            {
                lineChart.getData().remove(averageSeries4);
            }
        }
    }































}
