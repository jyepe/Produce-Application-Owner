package com.example.yepej.greenseasons;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class BaseApplication extends Application
{
    public static final String CHANNEL_1 = "Notification Channel";


    @Override
    public void onCreate()
    {
        super.onCreate();
        createNotifications();
        sendTokenToDB();
    }

    private void sendTokenToDB()
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        Firebase.setAndroidContext(this);
        Log.i("sending token to db", token);
        Firebase rootDB = new Firebase("https://fir-messaging-3c492.firebaseio.com/");
        Firebase orderNode = rootDB.child("Client Token");
        orderNode.setValue(token);
    }

    private void createNotifications()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);

            channel1.setDescription("Notification Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel1);

        }
    }
}
