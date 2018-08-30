package com.example.yepej.greenseasons;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.yepej.greenseasons.BaseApplication.CHANNEL_1;

public class MessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        showNotification();
    }

    private void showNotification()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_1)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Green Seasons")
                .setContentText("New customer order")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onNewToken(String s)
    {
        Log.i("new token", s);
        Firebase rootDB = new Firebase("https://fir-messaging-3c492.firebaseio.com/");
        Firebase orderNode = rootDB.child("Client Token");
        orderNode.setValue(s);
    }
}
