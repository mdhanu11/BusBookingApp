package com.example.busbookingapp.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Bus {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String busName;

    public Bus(String busName) {
        this.busName = busName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bus bus = (Bus) o;
        return id == bus.id && busName.equals(bus.busName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, busName);
    }

    @Override
    public String toString() {
        return "Bus{" +
                "bus_id=" + id +
                ", busName='" + busName + '\'' +
                '}';
    }
}
