package com.example.busbookingapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.busbookingapp.MainActivity;
import com.example.busbookingapp.R;
import com.example.busbookingapp.db.entities.SeatsAvailabiliy;

import java.util.ArrayList;


public class GridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<SeatsAvailabiliy> seatsArray;
    LayoutInflater inflter;

    public GridViewAdapter(Context applicationContext, ArrayList<SeatsAvailabiliy> seatsArray) {
        this.context = applicationContext;
        this.seatsArray = seatsArray;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return seatsArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View myView = view;
        if (myView == null){
            myView = LayoutInflater.from(context).inflate(R.layout.card_item, viewGroup, false);
        }

        SeatsAvailabiliy seatsAvailabiliy = seatsArray.get(i);
        CardView cardView = (CardView) myView.findViewById(R.id.cardView);
        if(seatsAvailabiliy.isAvailability()){
            cardView.setBackgroundColor(Color.parseColor("#00FF00"));
        }
        else{
            cardView.setBackgroundColor(Color.parseColor("#FFA500"));
        }
        if(seatsAvailabiliy.isSelected()){
            cardView.setBackgroundColor(Color.parseColor("#0000FF"));
        }
        return myView;
    }
}