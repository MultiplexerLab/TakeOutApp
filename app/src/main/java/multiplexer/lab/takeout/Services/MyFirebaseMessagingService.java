package multiplexer.lab.takeout.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.util.Map;

import multiplexer.lab.takeout.MainActivity;
import multiplexer.lab.takeout.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String channel = "Takeout Offer";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            showNotifications(remoteMessage);
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void showNotifications(final RemoteMessage remoteMessage) {
        Log.d("Dhurr2", remoteMessage.getNotification().getTitle());

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_image_only);

        remoteViews.setTextViewText(R.id.notificationTitle, remoteMessage.getNotification().getTitle());
        remoteViews.setTextViewText(R.id.notificationDes, remoteMessage.getNotification().getBody());
        final int notificationId = 1459756;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        final Notification notification = notificationBuilder.build();
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Picasso.with(MyFirebaseMessagingService.this)
                            .load(remoteMessage.getNotification().getLink())
                            .into(remoteViews, R.id.imageLarge, notificationId, notification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        notificationManager.notify(notificationId, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        try {
            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel(channel, "Ramadan Eid OFFER", NotificationManager.IMPORTANCE_HIGH);
            adminChannel.setDescription("50% Offer in Every Products");
            adminChannel.enableLights(true);
            adminChannel.setLightColor(Color.RED);
            adminChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(adminChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
