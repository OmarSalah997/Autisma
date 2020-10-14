package com.example.autisma;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import com.example.autisma.data.alarm_contract;
public class ReminderAlarmService extends BroadcastReceiver {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 42;
    Cursor cursor;
    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri uri = intent.getData();
        //Display a notification to view the task details
        Intent action = new Intent(context, Add_alarm.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if(uri != null){
            cursor = context.getContentResolver().query(uri, null, null, null, null);
        }

        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = alarm_contract.getColumnString(cursor, alarm_contract.AlarmReminderEntry.KEY_TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.ic_baseline_notifications_on_24)
                .setContentTitle(context.getString(R.string.alarm_notif))
                .setContentText(description)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel1";
            String description2 = "channel1Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description2);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
    /*
    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        //Display a notification to view the task details
        Intent action = new Intent(this, Add_alarm.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = alarm_contract.getColumnString(cursor, alarm_contract.AlarmReminderEntry.KEY_TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_baseline_notifications_on_24)
                .setContentTitle(getString(R.string.alarm_notif))
                .setContentText(description)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved()");

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        assert alarmService != null;
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
*/
}
