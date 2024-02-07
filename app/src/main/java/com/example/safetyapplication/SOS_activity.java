package com.example.safetyapplication;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class SOS_activity extends AppCompatActivity implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 3.5f; // Adjust this threshold as needed
    private static final int SHAKE_TIME_INTERVAL = 10000; // Adjust this time interval as needed
    private long lastShakeTime;
    float x, y, z;
    String phonenumber;
    Button numberReg, secModeStart, secModeStop, SOS;
    public String myLocation;
    FusedLocationProviderClient fusedLocationClient;
    SensorManager sensorManager;
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 102;
    private static final int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 103;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        createNotificationChannel();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        numberReg = findViewById(R.id.RegisterGaurd);
        secModeStart = findViewById(R.id.secModeStart);
        SOS = findViewById(R.id.sos);
        secModeStop = findViewById(R.id.secModeStop);
        secModeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity();
            }
        });

        secModeStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopActivity();
            }
        });
        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), selectNumber_activity.class);
                startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
            }

        });
        numberReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registration_activity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == RESULT_OK) {
            phonenumber = data.getStringExtra("phoneNumber");
            Toast.makeText(this, ""+phonenumber, Toast.LENGTH_SHORT).show();
            if (phonenumber != null && !phonenumber.isEmpty()) {
                String lastLocation = getLastLocation();
//                sendLocationViaSMS(phonenumber, lastLocation);
                Toast.makeText(this, "ph.no : "+phonenumber+"location is : "+lastLocation, Toast.LENGTH_SHORT).show();
                showNotification("Location sent to: " + phonenumber);
            } else {
                Toast.makeText(this, "Emergency contact not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        float eg = sensorManager.GRAVITY_EARTH;
        float acceleration = (x * x + y * y + z * z) / (eg * eg);
        if (acceleration > SHAKE_THRESHOLD) {
            long currentTime = System.currentTimeMillis();
            String contact_index = "1";
            if (currentTime - lastShakeTime > SHAKE_TIME_INTERVAL) {
                String recentContact = getEmergencyContact(contact_index);
                String lastloc = getLastLocation();
//                sendLocationViaSMS(recentContact,lastloc);
                showNotification("location send to :"+recentContact);
                lastShakeTime = currentTime;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private String getEmergencyContact(String contact_index) {
        SharedPreferences sharedPreferences = getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);
        String getContact = sharedPreferences.getString("number_"+contact_index, "");
        return getContact;
    }
    private String getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(SOS_activity.this);
        if (ActivityCompat.checkSelfPermission(SOS_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SOS_activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            location.getAltitude();
                            location.getLongitude();
                            myLocation = "http://maps.google.com/maps?q=loc:" + location.getLatitude() + "," + location.getLongitude();
                        } else {
                            Toast.makeText(SOS_activity.this, "location denied", Toast.LENGTH_SHORT).show();
                            myLocation = "Unable to Find Location :(";
                        }
                    }
                });
        return myLocation;
    }
    private void sendLocationViaSMS(String phoneNumber, String myLocation) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "I am in a trouble please help me \n " + myLocation, null, null);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to send location via SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void StartActivity() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        showNotification("Secure Mode Started");
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Accelerometer not available on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void StopActivity() {
        Toast.makeText(this, "Activity stopped successfully", Toast.LENGTH_SHORT).show();
        sensorManager.unregisterListener(this);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(1);
    }

    private void showNotification(String message) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                    .setSmallIcon(R.drawable.ic_android_notification_24dp)
                    .setContentTitle("Safe-App")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, PERMISSION_REQUEST_CODE);
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel("channelId", name, importance);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel.setDescription(description);
            }

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

