package com.aecb.util;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Build.VERSION;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.aecb.R;
import com.aecb.ui.splash.view.SplashActivity;

import java.util.Date;

import timber.log.Timber;

public final class NotificationUtils extends ContextWrapper {
    public static final String CHANNEL_MESSAGES = "Messages";
    public static NotificationUtils instance;
    private static NotificationManager mManager;
    public Context context;

    public NotificationUtils(Context context) {
        super(context);
        this.context = context;
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Timber.e("NotificationUtils: " + getManager().getNotificationChannel(CHANNEL_MESSAGES));
            if (getManager().getNotificationChannel(CHANNEL_MESSAGES) == null) {
                createChannel(CHANNEL_MESSAGES);
            }
        }
        instance = (NotificationUtils) this;
    }

    private static int getUniqueIdForNotification() {
        String tmpStr = String.valueOf((new Date()).getTime());
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        return Integer.valueOf(last4Str);
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(String channelName) {
        NotificationChannel androidChannel = new NotificationChannel(getChannelId(channelName),
                channelName, NotificationManager.IMPORTANCE_DEFAULT);
        androidChannel.enableLights(true);
        androidChannel.enableVibration(true);
        androidChannel.setLightColor(Color.BLUE);
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(androidChannel);
    }

    private String getChannelId(String channelName) {
        return getPackageName() + '.' + channelName;
    }

    public void postNotification(String title, String body) {
        Notification notification;
        PendingIntent contentIntent;
        String channelName = CHANNEL_MESSAGES;
        Intent intent = new Intent(context, SplashActivity.class);
        contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = (new Builder(context, getChannelId(channelName)))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setGroup(getChannelId(channelName))
                    .setGroupSummary(true)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = (new NotificationCompat.Builder(context, getChannelId(channelName)))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setGroup(getChannelId(channelName))
                    .setGroupSummary(true)
                    .setContentIntent(contentIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .build();
        }

        getManager().notify(channelName, getUniqueIdForNotification(), notification);
    }

    public final NotificationUtils getInstance() {
        return NotificationUtils.instance;
    }
}