package noelanthony.com.lostandfoundfinal.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import noelanthony.com.lostandfoundfinal.ChatMessagesActivity;
import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.loginregister.MainActivity;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;

/**
 * Created by Noel on 12/05/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServic";

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d(TAG,"onMessageReceived: Message Received: \n" + "Title: " + title + "\n" + "Message: " + message);
        sendNotification(title,message);
        //Check if message contains a data payload
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG,"Message data payload: " + remoteMessage.getData());
            try{
                JSONObject data = new JSONObject(remoteMessage.getData());
                String jsonMessage = data.getString("extra_information");
                Log.d(TAG,"onMessageReceived: \n" + "Extra Information: " +jsonMessage);
            }catch(JSONException e){
                e.printStackTrace();
            }

        }
        //check if message contains a notification payload
        if(remoteMessage.getNotification() != null){*/
        String title = remoteMessage.getNotification().getTitle(); //get title
        String message = remoteMessage.getNotification().getBody(); //get message
        String click_action = remoteMessage.getNotification().getClickAction(); //get click_action
        String posterName = remoteMessage.getData().get("item_poster");
        String posterId = remoteMessage.getData().get("item_uid");
        String menuFragmentValue = remoteMessage.getData().get("menuFragment");

        Log.d(TAG, "Message Notification Title: " + title);
        Log.d(TAG, "Message Notification Body: " + message);
        Log.d(TAG, "Message Notification click_action: " + click_action);


        Intent intent = new Intent(click_action);

        if (click_action.equals("chatmessage")) {
            intent = new Intent (this, ChatMessagesActivity.class);
            intent.putExtra("item_poster", posterName);
            intent.putExtra("item_uid", posterId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (click_action.equals("newsfeed")){
            //intent = new Intent (this, newsFeedActivity.class);
            intent = new Intent (this, newsFeedActivity.class);
            if(menuFragmentValue!=null) {
                intent.putExtra("menuFragment", menuFragmentValue);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // startActivity(intent);
        }else{
            intent = new Intent (this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);


        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.applogo)
                        .setContentTitle(title)
                        .setContentText(message)
                        //.setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);



        int mNotificationId = (int)System.currentTimeMillis();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notificationBuilder.build());
        //sendNotification(title, message, click_action);

    }

//}


    @Override
    public void onDeletedMessages() {

    }

/*
    private void sendNotification(String title, String messageBody, String click_action) {
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intent;

        if (click_action.equals("adminApprove")) {
            intent = new Intent (this, adminApprove.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (click_action.equals("newsfeed")){
           //intent = new Intent (this, newsFeedActivity.class);
            intent = new Intent (this, MainActivity.class);
           intent.putExtra("From", "notifyFrag");
          // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          // startActivity(intent);
        }else{
            intent = new Intent (this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification , notificationBuilder.build());
    }
*/

}
