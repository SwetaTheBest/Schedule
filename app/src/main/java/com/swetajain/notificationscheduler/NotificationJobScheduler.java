package com.swetajain.notificationscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

public class NotificationJobScheduler extends JobService {
    NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 11;
    public static final String NOTIFICATION_CHANNEL =
            "com.swetajain.notificationscheduler.NOTIFICATION_CHANNEL";

    public void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL,
                            "JOB NOTIFICATION CHANNEL",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Shows the job notification");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void deliverNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this,
                        NOTIFICATION_CHANNEL)
                        .setContentTitle("MY JOB NOTICE")
                        .setAutoCancel(true)
                        .setContentText("This is a job notice for your app")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_stat_name);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();
        PendingIntent contentPendingIntent =
                PendingIntent.getActivity(this,
                        NOTIFICATION_ID,
                        new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        deliverNotification();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        //to reschedule make it return true
        return true;
    }
}
