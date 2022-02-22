package com.example.busbookingapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.busbookingapp.db.entities.Bus;
import com.example.busbookingapp.db.entities.BusWithSeats;
import com.example.busbookingapp.db.entities.SeatsAvailabiliy;

import java.util.List;

@Dao
public interface BusDao {

    @Transaction
    @Insert
    long insert(Bus bus);

    @Insert
    void insertSeats(List<SeatsAvailabiliy> seats);

    @Query("SELECT * FROM Bus where id = :busId")
    @Transaction
    List<BusWithSeats> getBusDetails(int busId);

    @Query("Update SeatsAvailabiliy SET availability = 0, isSelected = 0 where busId = :busId and id = :seatId")
    void updateSeatsAfterBooked(long busId,int seatId);

    @Query("Update SeatsAvailabiliy SET isSelected = :selected where busId = :busId and id= :seatId")
    void updateSeatSelection(boolean selected,long busId,long seatId);


}
