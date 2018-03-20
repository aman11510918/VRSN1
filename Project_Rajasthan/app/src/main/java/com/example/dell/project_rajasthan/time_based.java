package com.example.dell.project_rajasthan;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class time_based extends AppCompatActivity {

    TextView t;
    Typeface type1;
    Spinner sp1,sp2;
    Button b,b2,b3;
    int hours=0,minutes=0,total,current_Hours,current_Minutes;
    EditText ed1;
    Integer arr1[]={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
    Integer arr2[]={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40
                    ,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_based);
        this.setTitle("Time Based Sticky Notes");
        type1= Typeface.createFromAsset(getAssets(),"aprilflowers.ttf");
        t= (TextView) findViewById(R.id.textView3);
        t.setTypeface(type1);
        b= (Button) findViewById(R.id.button2);
        b3= (Button) findViewById(R.id.button4);
        ed1= (EditText) findViewById(R.id.editText);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d=new Dialog(time_based.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.mydialog_timepicker, null);
                sp1= (Spinner) convertView.findViewById(R.id.spinner);
                sp2= (Spinner) convertView.findViewById(R.id.spinner2);
                sp1.setAdapter(new ArrayAdapter<Integer>(time_based.this,android.R.layout.simple_spinner_dropdown_item,arr1));
                sp2.setAdapter(new ArrayAdapter<Integer>(time_based.this,android.R.layout.simple_spinner_dropdown_item,arr2));
                sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        hours = arr1[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        minutes = arr2[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                b2= (Button) convertView.findViewById(R.id.button3);
                d.setContentView(convertView);
                d.setCancelable(false);
                d.show();
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        total=((hours*60*60)+(minutes*60))*1000;
                    //  Toast.makeText(time_based.this,"Event Scheduled after :"+((total/1000)/60)+" minutes" , Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals("")) {
                    ed1.setError("Enter a Message");
                } else {
                    scheduleNotification(getNotification(ed1.getText().toString()), total);
                    Toast.makeText(time_based.this, "Task Scheduled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Virtual Reality Sticky Notes");
        builder.setContentText(content);
        builder.setSmallIcon(R.color.colorAccent);
        return builder.build();
    }
}