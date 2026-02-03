package com.android.system.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.Notification;
import android.os.Build;
import android.widget.Toast;

public class NotifyReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "magisk_notify_channel";
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        if (title == null) title = "Magisk";
        if (text == null) text = "事件发生";

        // Android O+ 需要 channel
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = nm.getNotificationChannel(CHANNEL_ID);
            if (ch == null) {
                ch = new NotificationChannel(CHANNEL_ID, "Magisk Notifier", NotificationManager.IMPORTANCE_HIGH);
                nm.createNotificationChannel(ch);
            }
            Notification.Builder nb;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nb = new Notification.Builder(context, CHANNEL_ID);
            } else {
                nb = new Notification.Builder(context);
            }
            nb.setContentTitle(title)
              .setContentText(text)
              .setSmallIcon(android.R.drawable.ic_dialog_info)
              .setAutoCancel(true);
            nm.notify(1001, nb.build());
        } else {
            // 低版本或者 nm 为 null 时弹吐司作为回退
            Toast.makeText(context, title + ": " + text, Toast.LENGTH_LONG).show();
        }
    }
}
