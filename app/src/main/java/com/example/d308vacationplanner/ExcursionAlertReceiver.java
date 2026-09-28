package com.example.d308vacationplanner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ExcursionAlertReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "excursion_alerts";

    @Override
    public void onReceive(Context context, Intent intent) {

        String excursionTitle =
                intent.getStringExtra("excursionTitle");

        if (excursionTitle == null) {
            excursionTitle = "Excursion";
        }

        NotificationManager notificationManager =
                (NotificationManager)
                        context.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

        NotificationChannel channel =
                new NotificationChannel(
                        CHANNEL_ID,
                        "Excursion Alerts",
                        NotificationManager.IMPORTANCE_HIGH
                );

        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                android.R.drawable.ic_dialog_info
                        )
                        .setContentTitle(
                                "Excursion reminder"
                        )
                        .setContentText(
                                excursionTitle
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        )
                        .setAutoCancel(true);

        notificationManager.notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }
}
