package com.example.zt_orders.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zt_orders.ArchiveActivity;
import com.example.zt_orders.MainActivity;
import com.example.zt_orders.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class MyFireBaseInstanceService extends FirebaseMessagingService {

    private static final String LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE =
            "AAAAGPW2gDo:APA91bG1RS5g8a_SpTY2tQQGQuuOFirkbsFULploq7DrbxJdKhdR88II6svu6IaZg3hZyBQtu-3anbm19yXJJQoabJyhJExjcSt0JiGg12e4VQquU5cfdD6oBDrHttgq2_WK3xkPeCGZ";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Intent notificationIntent = new Intent(MyFireBaseInstanceService.this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MyFireBaseInstanceService.this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        if (remoteMessage.getData().isEmpty())
            showNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                    contentIntent);
        else
            showNotification(remoteMessage.getData(), contentIntent);
    }

    private void showNotification(Map<String, String> data, PendingIntent pendingIntent) {
        String title = data.get("title").toString();
        String body = data.get("body").toString();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.example.zt_orders.test";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("ZT Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_logo_zt_round)
                .setContentTitle(title)
                .setContentInfo("Info")
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo_zt))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                ;

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    private void showNotification(String title, String body, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.example.zt_orders.test";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("ZT Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_logo_zt_round)
            .setContentTitle(title)
            .setContentInfo("Info")
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo_zt))
            .setContentIntent(pendingIntent)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
            ;

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d("TOKENFIREBASE", s);
    }

    public static void sendPushToSingleInstance(final Context activity, final HashMap dataValue) {


        final String url = "https://fcm.googleapis.com/fcm/send";
        StringRequest myReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(activity, "Bingo Success", Toast.LENGTH_SHORT).show();
                        Log.d("Message send","Message has been sended");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show();
                        Log.d("Message send","Message doesn`t been sended");
                    }
                }) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Map<String, Object> rawParameters = new Hashtable();
                rawParameters.put("data", new JSONObject(dataValue));
//                rawParameters.put("to", "/topics/all");
                rawParameters.put("to", "/topics/private");
                return new JSONObject(rawParameters).toString().getBytes();
            };

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key="+LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE);
                headers.put("Content-Type","application/json");
                return headers;
            }

        };

        Volley.newRequestQueue(activity).add(myReq);
    }

}
