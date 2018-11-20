package multiplexer.lab.takeout.Services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.d("Dhurr", "From: " + remoteMessage.getFrom());

            if (remoteMessage.getData().size() > 0) {
                Log.d("Dhurr", "Message data payload: " + remoteMessage.getData());

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                    //scheduleJob();
                } else {
                    // Handle message within 10 seconds
                    // handleNow();
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d("Dhurr", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
}
