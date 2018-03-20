package com.example.dell.project_rajasthan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class checking extends AppCompatActivity {
    TextView t1,t2,t3;
    ImageView imgs1;
    ListView list1;
    ArrayList<String> listPermissionsNeeded;
    double longitude,latitude;
    double PIx = 3.141592653589793;
    double RADIUS = 6378.16;
    ArrayList<Double> lists,list2;
    ArrayList<String> list3,list4;
    SQLiteDatabase sqlite;
    Typeface type1,type2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);
        this.setTitle("Virtual Reality Sticky Notes");
        type1=Typeface.createFromAsset(getAssets(),"bewildet.ttf");
        type2=Typeface.createFromAsset(getAssets(),"aprilflowers.ttf");
        sqlite=openOrCreateDatabase("abc",MODE_PRIVATE,null);
        Cursor c= sqlite.rawQuery("select * from tab",null);
        lists=new ArrayList<Double>();
        list2=new ArrayList<Double>();
        list3=new ArrayList<String>();
        list4=new ArrayList<String>();
        while(c.moveToNext())
        {
            Double longitudes=c.getDouble(0);
            Double latitudes=c.getDouble(1);
            String messages=c.getString(2);
            String addresses=c.getString(3);
            lists.add(longitudes);
            list2.add(latitudes);
            list3.add(messages);
            list4.add(addresses);
        }

        list1 = (ListView) findViewById(R.id.lis);
        list1.setAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1, (ArrayList) list3));
    }

    class MyAdapter extends ArrayAdapter<ArrayList> {

        public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater myinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myview = myinflater.inflate(R.layout.custom_list, parent, false);
            t1 = (TextView) myview.findViewById(R.id.textView);
            t2 = (TextView) myview.findViewById(R.id.textView2);
            t3 = (TextView) myview.findViewById(R.id.textView4);
            imgs1= (ImageView) myview.findViewById(R.id.imageView3);
            myview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alerts=new AlertDialog.Builder(checking.this);
                    alerts.setTitle(list3.get(position));
                    alerts.show();
                }
            });
            t1.setText( list3.get(position));
            t1.setTypeface(type1);
            t2.setText( list4.get(position));
            t2.setTypeface(type2);
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(checking.this, ""+position, Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr="+list2.get(position).toString()+','+lists.get(position).toString()));
                    startActivity(i1);
                }
            });

            imgs1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlite.execSQL("delete from tab where longitude = "+lists.get(position).toString());
                    finish();
                }
            });
            return myview;
        }
    }


    double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {

        double dlon = Radians(lon2 - lon1);
        double dlat = Radians(lat2 - lat1);

        double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + Math.cos(Radians(lat1))
                * Math.cos(Radians(lat2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2));
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return angle * RADIUS;
    }
    double Radians(double x)
    {
        return x * PIx / 180;
    }
}