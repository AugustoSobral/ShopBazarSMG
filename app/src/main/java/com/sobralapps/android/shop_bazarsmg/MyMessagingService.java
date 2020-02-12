package com.sobralapps.android.shop_bazarsmg;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Pegamos os dados no formato definido na function escrita em JavaScript
        Map<String, String> data = remoteMessage.getData();

        if(data==null)
            return;

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("NOTIFICATION", "NOTIFICATION");

        //PendingIntent é um intent que aguarda uma ação ser executada
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notification_channel_id = "notification_channel_01";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =
                    new NotificationChannel(notification_channel_id, "My notifications", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Notificação Livre Mercado");
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //Para as versões antigas
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), notification_channel_id);
        builder.setAutoCancel(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder.setSmallIcon(R.drawable.ic_shopping_car_white_16dp);
        else
            builder.setSmallIcon(R.drawable.logo_letras_maiores_circular);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo_letras_maiores_circular));
        builder.setContentTitle(data.get("title"));
        builder.setContentText(data.get("body"));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());

    }


}
