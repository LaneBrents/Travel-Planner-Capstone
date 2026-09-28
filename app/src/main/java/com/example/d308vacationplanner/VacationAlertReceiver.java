package com.example.d308vacationplanner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class VacationAlertReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "vacation_alerts";

    @Override
    public void onReceive(Context context, Intent intent) {

        String vacationTitle = intent.getStringExtra("vacationTitle");
        String alertType = intent.getStringExtra("alertType");

        if (vacationTitle == null) {
            vacationTitle = "Vacation";
        }

        if (alertType == null) {
            alertType = "Vacation reminder";
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Vacation Alerts",
                NotificationManager.IMPORTANCE_HIGH
        );

        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(alertType)
                        .setContentText(vacationTitle)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        notificationManager.notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }
}
