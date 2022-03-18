package com.example.project;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TDP extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    Button button, submitBtn;
    EditText time, date, address;
    String type, uid;
    DatabaseReference taskDB, workersDB, usersDB;
    int year, month, dayOfMonth, hourOfDay, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdp);

        hourOfDay = -1;
        year = -1;

        type = getIntent().getStringExtra("type");
        taskDB = FirebaseDatabase.getInstance().getReference("Tasks");
        workersDB = FirebaseDatabase.getInstance().getReference("Workers");
        usersDB = FirebaseDatabase.getInstance().getReference("Users");

        button=findViewById(R.id.back);
        submitBtn = findViewById(R.id.submitBtn);

        SharedPreferences preferences=getSharedPreferences("UID",MODE_PRIVATE);
        uid=preferences.getString("uid","");

       // time = findViewById(R.id.time);

        Button button2 = (Button) findViewById(R.id.button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
       // date = findViewById(R.id.date);

        Button button3 = (Button) findViewById(R.id.button2);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        address = findViewById(R.id.address);
        submitBtn = findViewById(R.id.submitBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TDP.this,Drawer.class);
                startActivity(intent);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmpty = true;
                if(address.getText().toString().isEmpty()){
                    address.setError("This field can't be empty");
                    isEmpty = false;
                }

                if(hourOfDay < 0 || year < 0){
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    isEmpty = false;
                }

                Date currDate = new Date();

                if(year < currDate.getYear()+1900){
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    isEmpty = false;
                }
                if(year >= currDate.getYear()+1900 && month < currDate.getMonth()){
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    isEmpty = false;
                }
                if(year >= currDate.getYear()+1900 && month >= currDate.getMonth() && dayOfMonth < currDate.getDay()+1){
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    isEmpty = false;
                }

                if(isEmpty) {
                    scheduleTask();
                }
            }
        });

        setAddress();
    }

    void setAddress(){

        usersDB.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                address.setText("House no "+user.getHouse()+", Street no "+user.getStreet()+
                        ", Phase no "+ user.getSector()+", "+user.getArea()+", "+user.getCity()+ " Pakistan");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.textview2);
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        textView.setText("Hour: " + hourOfDay + " Minute: " + minute);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        TextView textView = (TextView) findViewById(R.id.textview3);
        textView.setText(currentDateString);
    }


    void scheduleTask(){

        (findViewById(R.id.pbar1)).setVisibility(View.VISIBLE);
        final Workers[] worker = new Workers[1];
        workersDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAssigned = false;
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    worker[0] = i.getValue(Workers.class);
                    if(worker[0].getType().equalsIgnoreCase(type) && worker[0].getAcquired() == false) {
                        ScheduleTaskModel task = new ScheduleTaskModel(year, month, dayOfMonth, hourOfDay, minute, worker[0].getUid(), uid, address.getText().toString(), worker[0].getName(), worker[0].getType());
                        task.setTaskUid(UUID.randomUUID().toString());
                        taskDB.child(task.getTaskUid()).setValue(task);
                        //Toast.makeText(getApplicationContext(), worker[0].getName()+ " "+type+ " is assigned to you!", Toast.LENGTH_LONG).show();
                        showNotification("Job Scheduled", worker[0].getName()+ " "+type.toLowerCase()+ " is assigned to you!");
                        workersDB.child(worker[0].getUid()).child("acquired").setValue(true);
                        isAssigned = true;
                        break;
                    }
                }
                if(!isAssigned) {
                    Toast.makeText(getApplicationContext(), "Sorry, currently no person available", Toast.LENGTH_LONG).show();
                }
                (findViewById(R.id.pbar1)).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showNotification(String title, String message){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "alarm",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification channel for scheduled services");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.drawable.hman_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
