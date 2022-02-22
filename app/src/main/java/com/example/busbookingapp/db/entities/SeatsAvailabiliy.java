package com.example.busbookingapp.db.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class SeatsAvailabiliy {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = Bus.class,
            parentColumns = "id",
            childColumns = "bus_id",
            onDelete = CASCADE)
    private long busId;

    private boolean availability;

    private boolean isSelected;

    public SeatsAvailabiliy(long busId, boolean availability, boolean isSelected) {
        this.id = id;
        this.busId = busId;
        this.availability = availability;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public long getBusId() {
        return busId;
    }

    public void setBusId(long busId) {
        this.busId = busId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "SeatsAvailabiliy{" +
                "id=" + id +
                ", busId=" + busId +
                ", availability=" + availability +
                ", isSelected=" + isSelected +
                '}';
    }
}
