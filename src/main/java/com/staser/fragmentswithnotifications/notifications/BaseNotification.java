package com.staser.fragmentswithnotifications.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.staser.fragmentswithnotifications.MainActivity;
import com.staser.fragmentswithnotifications.R;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.staser.fragmentswithnotifications.common.Common.CHAN_ID;
import static com.staser.fragmentswithnotifications.common.Common.CHAN_NAME;
import static com.staser.fragmentswithnotifications.common.Common.INTENT_EXTRAS;
import static com.staser.fragmentswithnotifications.common.Common.NOTIFICATION;
import static com.staser.fragmentswithnotifications.common.Common.NOTIFICATION_TITLE;

public class BaseNotification extends Notification {

    /**
     * constructor with creating intents
     * @param context
     * @param frgNum the counted notification ID
     * @param nftNum the counted fragment Number
     */
    public BaseNotification(Context context, int frgNum, int nftNum) {
        String msg = NOTIFICATION + " " +String.valueOf(frgNum)/* + " - count: " + String.valueOf(nftNum)*/;
        Intent intent  = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        intent.putExtra(INTENT_EXTRAS, frgNum);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, frgNum, intent, PendingIntent.FLAG_ONE_SHOT);
        this.createNotification(
                context,
                msg,
                nftNum,
                frgNum,
                pendingIntent);

    }

    /**
     * creating notification for wto type OS (before) and (after or equals) 26  and notify user
     * @param context
     * @param msg
     * @param ntfId the counted notification ID
     * @param frgId the counted fragment Number
     * @param pendingIntent
     */
    private void createNotification(Context context, String msg, int ntfId, int frgId,  PendingIntent pendingIntent) {
        Builder nBuilder;
        NotificationManager nManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHAN_ID, CHAN_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("this is description");
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            nManager.createNotificationChannel(channel);
            nBuilder = new Builder(context, CHAN_ID)
                    .setChannelId(CHAN_ID)
                    .setSmallIcon(R.drawable.n)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(msg)
                    .setAutoCancel(true);

        } else {
            nBuilder = new Builder(context)
                    .setSmallIcon(R.drawable.n)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(msg)
                    .setAutoCancel(true);
        }

        nBuilder.setContentIntent(pendingIntent);
        nManager.notify(
                String.valueOf(frgId),
                ntfId,
                nBuilder.build());
    }



}
