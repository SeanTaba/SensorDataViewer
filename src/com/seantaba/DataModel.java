package com.seantaba;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

import java.io.Serializable;

public class DataModel implements Serializable
{
    private final SimpleLongProperty time;
    private final SimpleIntegerProperty sensor1;
    private final SimpleIntegerProperty sensor2;
    private final SimpleIntegerProperty sensor3;
    private final SimpleIntegerProperty sensor4;

    public DataModel(SimpleLongProperty time, SimpleIntegerProperty sensor1, SimpleIntegerProperty sensor2,
                     SimpleIntegerProperty sensor3, SimpleIntegerProperty sensor4)
    {
        this.time = time;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
    }

    public Long getTime() {
        return time.get();
    }

    public SimpleLongProperty timeProperty() {
        return time;
    }

    public int getSensor1()
    {
        return sensor1.get();
    }

    public SimpleIntegerProperty sensor1Property()
    {
        return sensor1;
    }

    public void setSensor1(int sensor1)
    {
        this.sensor1.set(sensor1);
    }

    public int getSensor2()
    {
        return sensor2.get();
    }

    public SimpleIntegerProperty sensor2Property()
    {
        return sensor2;
    }

    public void setSensor2(int sensor2)
    {
        this.sensor2.set(sensor2);
    }

    public int getSensor3()
    {
        return sensor3.get();
    }

    public SimpleIntegerProperty sensor3Property()
    {
        return sensor3;
    }

    public void setSensor3(int sensor3)
    {
        this.sensor3.set(sensor3);
    }

    public int getSensor4()
    {
        return sensor4.get();
    }

    public SimpleIntegerProperty sensor4Property()
    {
        return sensor4;
    }

    public void setSensor4(int sensor4)
    {
        this.sensor4.set(sensor4);
    }
}
