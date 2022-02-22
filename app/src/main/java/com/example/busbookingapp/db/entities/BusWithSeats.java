package com.example.busbookingapp.db.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class BusWithSeats {


    @Embedded
    public Bus bus;
    @Relation(
            parentColumn = "id",
            entityColumn = "busId"
    )
    public List<SeatsAvailabiliy> buses;

//    public BusWithSeats(Bus bus, List<SeatsAvailabiliy> buses) {
//        this.bus = bus;
//        this.buses = buses;
//    }
}
