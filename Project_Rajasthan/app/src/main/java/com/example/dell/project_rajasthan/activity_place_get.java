package com.example.dell.project_rajasthan;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class activity_place_get extends AppCompatActivity {
    Button b,b2;
    Place place;
    TextView t;
    Double longi,latti;
    EditText ed1;
    Boolean flag1,flag2;
    Typeface type1;
    String mess,address;
    SQLiteDatabase sqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_get);
        b= (Button) findViewById(R.id.button6);
        b2= (Button) findViewById(R.id.button7);
        t= (TextView) findViewById(R.id.textView8);
        ed1= (EditText) findViewById(R.id.editText2);
        mess= ed1.getText().toString();
        type1=Typeface.createFromAsset(getAssets(),"aprilflowers.ttf");
        t.setTypeface(type1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(activity_place_get.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().isEmpty())
                {
                    ed1.setError("Enter a Message");
                }
                else
                {
                    flag1=true;
                }
                if(place==null)
                {
                    Toast.makeText(activity_place_get.this,"Select a place",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    flag2=true;
                }
                if(flag1 == true && flag2 == true)
                {
                    sqlite=openOrCreateDatabase("abc",MODE_PRIVATE,null);
                    sqlite.execSQL("insert into tab values('"+longi+"','"+latti+"','"+ed1.getText().toString()+"','"+address+"')");
                    Toast.makeText(activity_place_get.this, "data saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                address = String.format("Place :%s", place.getAddress());
                longi = place.getLatLng().longitude;
                latti = place.getLatLng().latitude;
                t.setText("address :"+address+" \n"+" longitude : "+longi+" \n"+" latitude : "+latti);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}