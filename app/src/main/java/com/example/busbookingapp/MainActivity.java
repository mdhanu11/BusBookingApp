package com.example.busbookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.busbookingapp.adapters.GridViewAdapter;
import com.example.busbookingapp.db.AppDatabase;
import com.example.busbookingapp.db.dao.BusDao;
import com.example.busbookingapp.db.entities.Bus;
import com.example.busbookingapp.db.entities.BusWithSeats;
import com.example.busbookingapp.db.entities.SeatsAvailabiliy;
import com.example.busbookingapp.notification.LocalNotifcationPublisher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppDatabase busDatabase;
    private BusDao busDao;
//    private Bus bus;
    ArrayList<SeatsAvailabiliy> seatsArray = new ArrayList<>();
    ArrayList<SeatsAvailabiliy> selectedSeatsArray = new ArrayList<>();
    GridView gridView;
    Button bookTicketButton;
    EditText dateText,timeText;
    final Calendar calendar= Calendar.getInstance();


    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busDatabase = AppDatabase.getInstance(MainActivity.this);
        busDao = busDatabase.getBusDao();

        //To save predefined data, un comment this and run the app.
//        saveBusDetails();

        getBusese();

        gridView = (GridView) findViewById(R.id.gridview);
        bookTicketButton = (Button) findViewById(R.id.book_button);

        bookTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookButtonClicked(v);
            }
        });


        GridViewAdapter gridViewAdapter = new GridViewAdapter(MainActivity.this, seatsArray);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MainActivity", "Clicked - " + view);
                SeatsAvailabiliy seatsAvailabiliy = seatsArray.get(position);
                CardView cardView = (CardView) view.findViewById(R.id.cardView);
                Log.e("MainActivity", "id - " + seatsAvailabiliy.getId());

                if(selectedSeatsArray.size() <= 1){
                    if(seatsAvailabiliy.isAvailability()){
                        if(seatsAvailabiliy.isSelected()){
                            busDao.updateSeatSelection(false,seatsAvailabiliy.getBusId(),seatsAvailabiliy.getId());
                            cardView.setBackgroundColor(Color.parseColor("#0000FF"));
                            seatsAvailabiliy.setSelected(false);
                            selectedSeatsArray.remove(seatsAvailabiliy);
                        }
                        else{
                            busDao.updateSeatSelection(true,seatsAvailabiliy.getBusId(),seatsAvailabiliy.getId());
                            cardView.setBackgroundColor(Color.parseColor("#00FF00"));
                            seatsAvailabiliy.setSelected(true);
                            selectedSeatsArray.add(seatsAvailabiliy);
                        }

                        seatsArray.set(position,seatsAvailabiliy);
                        gridViewAdapter.notifyDataSetChanged();
                    }
                    else{
                        Log.e("MainActivity", "Seat already booked");
                    }
                }
                else{
                    Log.e("MainActivity", "You cant book more than one seat");
                }


            }
        });

    }

    public void bookButtonClicked(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_view, null);

        dateText=(EditText) popupView.findViewById(R.id.date_picker);
        timeText=(EditText) popupView.findViewById(R.id.time_picker);


        Button bookNowButton = (Button) popupView.findViewById(R.id.book_popup_button);
        Button cancelButton = (Button) popupView.findViewById(R.id.cancel_button);

        setDatePicker();
        setTimePicker();

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                return true;
//            }
//        });

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSeats();
                popupWindow.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void updateSeats(){

        /*
        1. Update seats avaialability
        2. Update seat selection
        3. Set Local Notification
         */
        for (SeatsAvailabiliy selectedSeat : selectedSeatsArray) {
            busDao.updateSeatsAfterBooked(1,selectedSeat.getId());
        }
        this.recreate();
        Log.e("MainActivity", "selected time - " + calendar.getTimeInMillis());
        scheduleNotification(getNotification("Ticket Booked"),calendar.getTimeInMillis(),MainActivity.this);
    }
    private void scheduleNotification (Notification notification , long delay, Context context) {
        Intent notificationIntent = new Intent(context, LocalNotifcationPublisher.class ) ;
        notificationIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        notificationIntent.putExtra(LocalNotifcationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(LocalNotifcationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( context, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    private void setDatePicker(){
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void setTimePicker(){

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeText.setText(hourOfDay + ":" + minute);
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(dateFormat.format(calendar.getTime()));
    }

    void  getBusese(){
        List<BusWithSeats> buses = busDao.getBusDetails(1);
        Log.e("MainActivity", "buses - " + buses.toString());
        for (BusWithSeats busWithSeat : buses) {
            seatsArray.addAll(busWithSeat.buses);
        }
        Log.e("MainActivity", "seatsArray - " + seatsArray);
    }


    void saveBusDetails(){

        Bus busDetails = new Bus("SLV Travels");

        long busId = busDao.insert(busDetails);

        List<SeatsAvailabiliy> seatsArray = new ArrayList<SeatsAvailabiliy>() ;
        for (int i=0;i<50;i++){
            if(i%2 == 0){
                SeatsAvailabiliy seat = new SeatsAvailabiliy(busId,true,false);
                seatsArray.add(seat);
            }
            else{
                SeatsAvailabiliy seat = new SeatsAvailabiliy(busId,false,false);
                seatsArray.add(seat);
            }
        }
        busDao.insertSeats(seatsArray);
    }
}