package com.seantaba;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class DataModel implements Serializable
{
    private final SimpleStringProperty time;
    private final SimpleStringProperty sensor1;
    private final SimpleStringProperty sensor2;
    private final SimpleStringProperty sensor3;
    private final SimpleStringProperty sensor4;

    public DataModel(SimpleStringProperty time, SimpleStringProperty sensor1, SimpleStringProperty sensor2,
                     SimpleStringProperty sensor3, SimpleStringProperty sensor4)
    {
        this.time = time;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public String getSensor1() {
        return sensor1.get();
    }

    public SimpleStringProperty sensor1Property() {
        return sensor1;
    }

    public String getSensor2() {
        return sensor2.get();
    }

    public SimpleStringProperty sensor2Property() {
        return sensor2;
    }

    public String getSensor3() {
        return sensor3.get();
    }

    public SimpleStringProperty sensor3Property() {
        return sensor3;
    }

    public String getSensor4() {
        return sensor4.get();
    }

    public SimpleStringProperty sensor4Property() {
        return sensor4;
    }
}
