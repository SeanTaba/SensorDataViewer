package com.seantaba;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

import java.util.ArrayList;
import java.util.List;

public class RawDataLineChartController
{
    private ObservableList<DataModel> data;
    private XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    private XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    private XYChart.Series<String, Number> series3 = new XYChart.Series<>();
    private XYChart.Series<String, Number> series4 = new XYChart.Series<>();
    private XYChart.Series<String, Number> averageSeries1 = new XYChart.Series<>();
    private XYChart.Series<String, Number> averageSeries2 = new XYChart.Series<>();
    private XYChart.Series<String, Number> averageSeries3 = new XYChart.Series<>();
    private XYChart.Series<String, Number> averageSeries4 = new XYChart.Series<>();
    private int windowWidth = 5;
    private boolean windowWidthChanged = false;
    private List<XYChart.Series<String,Number>> seriesList = new ArrayList<>();

    @FXML
    private LineChart<String, Number> lineChart;
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
    @FXML
    private Slider windowWidthSlider;

    public void initialize()
    {
        lineChartXAxis.setLabel("Time (ms)");
        lineChartXAxis.setAnimated(false);
        lineChartYAxis.setLabel("Value");
        lineChartYAxis.setAnimated(false);

        lineChart.setTitle("Raw Sensor Data");
        lineChart.setAnimated(false);

        windowWidthSlider.setMax(100);
        windowWidthSlider.setMin(5);

        seriesList.add(series1);
        seriesList.add(series2);
        seriesList.add(series3);
        seriesList.add(series4);
        seriesList.add(averageSeries1);
        seriesList.add(averageSeries2);
        seriesList.add(averageSeries3);
        seriesList.add(averageSeries4);
    }

    public void setup(ObservableList<DataModel> data)
    {
        this.data = data;

        series1.setName("Sensor 1");
        series2.setName("Sensor 2");
        series3.setName("Sensor 3");
        series4.setName("Sensor 4");
        averageSeries1.setName("Average Sensor 1");
        averageSeries2.setName("Average Sensor 2");
        averageSeries3.setName("Average Sensor 3");
        averageSeries4.setName("Average Sensor 4");



        for (XYChart.Series<String,Number> series: seriesList)
        {
            lineChart.getData().add(series);
        }

        data.addListener((ListChangeListener<DataModel>) c ->
        {
            if (windowWidthChanged)
            {
                for (XYChart.Series<String,Number> series: seriesList)
                {
                    series.getData().clear();
                }
                windowWidthChanged = false;
                windowWidth = (int) windowWidthSlider.getValue();
                for (int i = data.size() - windowWidth; i < data.size(); i++)
                {
                    if (i < 0) i = 0;
                    series1.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), data.get(i).getSensor1()));
                    series2.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), data.get(i).getSensor2()));
                    series3.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), data.get(i).getSensor3()));
                    series4.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), data.get(i).getSensor4()));

                    if (series1.getData().size() < 5) continue;
                    averageSeries1.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), getAverage(series1, series1.getData().size())));
                    if (averageSeries1.getData().size() > windowWidth) averageSeries1.getData().remove(0);
                    averageSeries2.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), getAverage(series2, series2.getData().size())));
                    if (averageSeries2.getData().size() > windowWidth) averageSeries2.getData().remove(0);
                    averageSeries3.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), getAverage(series3, series3.getData().size())));
                    if (averageSeries3.getData().size() > windowWidth) averageSeries3.getData().remove(0);
                    averageSeries4.getData().add(new XYChart.Data<>(data.get(i).getTime().toString(), getAverage(series4, series4.getData().size())));
                    if (averageSeries4.getData().size() > windowWidth) averageSeries4.getData().remove(0);
                }
                return;
            }
            series1.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), data.get(data.size() - 1).getSensor1()));
            if (series1.getData().size() > windowWidth) series1.getData().remove(0);
            series2.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), data.get(data.size() - 1).getSensor2()));
            if (series2.getData().size() > windowWidth) series2.getData().remove(0);
            series3.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), data.get(data.size() - 1).getSensor3()));
            if (series3.getData().size() > windowWidth) series3.getData().remove(0);
            series4.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), data.get(data.size() - 1).getSensor4()));
            if (series4.getData().size() > windowWidth) series4.getData().remove(0);

            if (series1.getData().size() < 5) return;

            averageSeries1.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), getAverage(series1, series1.getData().size())));
            if (averageSeries1.getData().size() > windowWidth) averageSeries1.getData().remove(0);
            averageSeries2.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), getAverage(series2, series2.getData().size())));
            if (averageSeries2.getData().size() > windowWidth) averageSeries2.getData().remove(0);
            averageSeries3.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), getAverage(series3, series3.getData().size())));
            if (averageSeries3.getData().size() > windowWidth) averageSeries3.getData().remove(0);
            averageSeries4.getData().add(new XYChart.Data<>(data.get(data.size() - 1).getTime().toString(), getAverage(series4, series4.getData().size())));
            if (averageSeries4.getData().size() > windowWidth) averageSeries4.getData().remove(0);
        });
    }

    public int getAverage(XYChart.Series<String,Number> series, int size)
    {
        int seriesAverage = 0;
        for (int i = size - 1; i > (size - 6); i--)
        {
            seriesAverage += series.getData().get(i).getYValue().intValue();
        }
        return seriesAverage /= 5;
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

    public void sliderHandler()
    {
        if ((int) windowWidthSlider.getValue() != windowWidth)
        {
            windowWidthChanged = true;
            windowWidth = (int) windowWidthSlider.getValue();
        }
        System.out.println("Window width was updated to: " + windowWidth);
    }


}
