package com.example.hydration.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.hydration.MainActivity;
import com.example.hydration.R;
import com.example.hydration.sync.ReminderTasks;
import com.example.hydration.sync.WaterReminderIntentService;

/**
 * Utility class for creating hydration notifications
 */
public class NotificationUtils {
    //This pending intent id is used to uniquely reference the pending intent
    private static final int WATER_REMINDER_PENDING_ID = 1376;

    private static final int IGNORE_WATER_REMINDER_PENDING_ID = 1476;
    private static final int ACTION_DRINK_PENDING_ID = 1576;
    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;
    //id of the channel to which this notification belongs to
    // This notification channel id is used to link notifications to this channel
    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder-notification-channel";

    // TODO (7) Create a method called remindUserBecauseCharging which takes a Context.
    public static void remindUserBecauseCharging(Context context) {
        // This method will create a notification for charging. It might be helpful
        // to take a look at this guide to see an example of what the code in this method will look like:
        // https://developer.android.com/training/notify-user/build-notification.html

        // TODO (8) Get the NotificationManager using context.getSystemService
        NotificationManager nfm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //afetrr the launch of oreo it is not posiible to launch the notification without it belonf=ging to the specific channel

        // TODO (9) Create a notification channel for Android O devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                            context.getString(R.string.main_notification_channel_name),
                            // this is for the O or later versions
                            NotificationManager.IMPORTANCE_HIGH
                    );

            nfm.createNotificationChannel(notificationChannel);
        }

        // TODO (10) In the remindUserBecauseCharging method use NotificationCompat.Builder to create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_cancel_black_24px)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentInfo(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.charging_reminder_notification_body)))
                //to set the default style to the vibration we need to add permission in the manifests
                .setDefaults(Notification.DEFAULT_VIBRATE)
                //what application to launch when clicked
                .setContentIntent(contentIntent(context))
                //  TODO (17) Add the two new actions using the addAction method and your helper methods
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);
        // when the application is launched the notification is automatically canceled
        //it will simply go away when it is clicked.setAutoCancel(true);
        // that:
        // - has a color of R.color.colorPrimary - use ContextCompat.getColor to get a compatible color
        // - has ic_drink_notification as the small icon
        // - uses icon returned by the largeIcon helper method as the large icon
        // - sets the title to the charging_reminder_notification_title String resource
        // - sets the text to the charging_reminder_notification_body String resource
        // - sets the style to NotificationCompat.BigTextStyle().bigText(text)
        // - sets the notification defaults to vibrate
        // - uses the content intent returned by the contentIntent helper method for the contentIntent
        // - automatically cancels the notification when the notification is clicked
        // TODO (11) If the build version is greater than or equal to JELLY_BEAN and less than OREO,
//since we have set the NotificationManager to high priority but that is only for the OREO or later
// versions so for backward compatibility just have a check
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        // TODO (12) Trigger the notification by calling notify on the NotificationManager.
        nfm.notify(WATER_REMINDER_PENDING_ID, builder.build());
//this id helps to cancel the notification later if we need
        // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
    }

    //  TODO (5) Add a static method called ignoreReminderAction
    public static NotificationCompat.Action ignoreReminderAction(Context context) {
        //      TODO (6) Create an Intent to launch WaterReminderIntentService
        Intent intent = new Intent(context, WaterReminderIntentService.class);
        //      TODO (7) Set the action of the intent to designate you want to dismiss the notification
        intent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        //      TODO (8) Create a PendingIntent from the intent to launch WaterReminderIntentService

        PendingIntent pendingIntent = PendingIntent.getService(context, IGNORE_WATER_REMINDER_PENDING_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //      TODO (9) Create an Action for the user to ignore the notification (and dismiss it)
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px, "No!!Thanks", pendingIntent);
        //      TODO (10) Return the action
        return action;
    }


    //  TODO (11) Add a static method called drinkWaterAction

    public static NotificationCompat.Action drinkWaterAction(Context context) {

        //      TODO (12) Create an Intent to launch WaterReminderIntentService
Intent  incrementWaterCountIntent=new Intent(context,WaterReminderIntentService.class);
    //      TODO (13) Set the action of the intent to designate you want to increment the water count
        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
    //      TODO (14) Create a PendingIntent from the intent to launch WaterReminderIntentService
        PendingIntent incrementWaterpendingIntent=PendingIntent.getService(context,ACTION_DRINK_PENDING_ID,incrementWaterCountIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    //      TODO (15) Create an Action for the user to tell us they've had a glass of water
NotificationCompat.Action action=new NotificationCompat.Action(R.drawable.ic_local_drink_grey_120px,"I did it!!",incrementWaterpendingIntent);
    //      TODO (16) Return the action
return action;
}

    // TODO (1) Create a helper method called contentIntent with a single parameter for a Context. It
    // should return a PendingIntent. This method will create the pending intent which will trigger when
    // the notification is pressed. This pending intent should open up the MainActivity.
    private static PendingIntent contentIntent(Context context) {
    // TODO (2) Create an intent that opens up the MainActivity
    Intent intent = new Intent(context, MainActivity.class);
    // TODO (3) Create a PendingIntent using getActivity that:
PendingIntent pendingIntent=PendingIntent
        .getActivity(context,WATER_REMINDER_PENDING_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    // - Take the context passed in as a parameter
    // - Takes an unique integer ID for the pending intent (you can create a constant for
    //   this integer above
    // - Takes the intent to open the MainActivity you just created; this is what is triggered
    //   when the notification is triggered
    // - Has the flag FLAG_UPDATE_CURRENT, so that if the intent is created again, keep the
    // intent but update the data
return pendingIntent;
}
    // TODO (4) Create a helper method called largeIcon which takes in a Context as a parameter and
    // returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.
  private static   Bitmap largeIcon(Context context) {
      // TODO (5) Get a Resources object from the context.
      Resources res = context.getResources();
// TODO (6) Create and return a bitmap using BitmapFactory.decodeResource, passing in the
      return BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
  }
  public static void clearAllNotifications(Context context)
  {
      NotificationManager nfm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
      nfm.cancelAll();
  }
}

