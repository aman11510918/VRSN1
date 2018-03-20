package com.example.dell.project_rajasthan;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class activity_home extends AppCompatActivity {
    Place place;
    ImageView img1, img2,img3;
    SQLiteDatabase sqlite;
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private static final String TAG = activity_home.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean mAlreadyStartedService = false;
    ArrayList<Double> lists,list2;
    ArrayList<String> list3,list4;
    TextView tv1,tv2,tv3;
    Typeface type1;
    Bitmap icon1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        this.setTitle("Virtual Reality Sticky Notes");
        icon1 = BitmapFactory.decodeResource(getResources(),R.mipmap.logo_app);
        type1 = Typeface.createFromAsset(getAssets(), "bewildet.ttf");
        img1 = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        tv1= (TextView) findViewById(R.id.textView6);
        tv2= (TextView) findViewById(R.id.textView7);
        tv3= (TextView) findViewById(R.id.textView8);
        tv1.setTypeface(type1);
        tv2.setTypeface(type1);
        tv3.setTypeface(type1);
        sqlite = openOrCreateDatabase("abc", MODE_PRIVATE, null);
        sqlite.execSQL("create table if not exists tab(longitude double,latitude double,message varchar,address varchar)");
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
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(my_service.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(my_service.EXTRA_LONGITUDE);
                        if (latitude != null && longitude != null) {
                            for (int i = 0; i < lists.size(); i++) {
                                    if (Math.abs(Double.valueOf(latitude)-list2.get(i))<0.3 &&
                                                            Math.abs(Double.valueOf(longitude)-lists.get(i))<0.3) {

                                     NotificationCompat.BigTextStyle bigT=new NotificationCompat.BigTextStyle().
                                             setBigContentTitle("VRSN").setSummaryText(list4.get(i));
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.
                                            Builder(activity_home.this).setSmallIcon(R.mipmap.logo_app)
                                            .setContentTitle("Virtual Reality Sticky Notes")
                                            .setContentText("Your message :"+list3.get(i))
                                            .setStyle(bigT).setLargeIcon(icon1);
                                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                                        mBuilder.setLights(Color.RED, 3000, 3000);
                                        mBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
                                        nm.notify(0, mBuilder.build());
                                    }
                            }
                        }
                    }
                }, new IntentFilter(my_service.ACTION_LOCATION_BROADCAST)
        );
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_home.this, activity_place_get.class));
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_home.this, checking.class));
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_home.this, time_based.class));
            }
        });
    }

    private void startStep1() {
        if (isGooglePlayServicesAvailable()) {
            startStep2(null);
        } else {
            Toast.makeText(getApplicationContext(),"No google play service available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startStep1();
    }

    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (checkPermissions()) {
            startStep3();
        } else {
            requestPermissions();
        }
        return true;
    }

    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_home.this);
        builder.setTitle("Title alert no alert");
        builder.setMessage("Msg alert not Internet");
        String positiveText = "Refresh";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (startStep2(dialog)) {
                            if (checkPermissions()) {
                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startStep3() {
        if (!mAlreadyStartedService) {
            Intent intent = new Intent(this, my_service.class);
            startService(intent);
            mAlreadyStartedService = true;
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(activity_home.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(activity_home.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
/*
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
*/
        Toast.makeText(this, "snackbar started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startStep3();
            } else {
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, my_service.class));
        mAlreadyStartedService = false;
        super.onDestroy();
    }
}