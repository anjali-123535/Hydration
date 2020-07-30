package com.example.hydration.sync;

import android.content.ContentResolver;
import android.content.Context;

import com.example.hydration.utilities.NotificationUtils;
import com.example.hydration.utilities.PreferenceUtilities;

// TODO (1) Create a class called ReminderTasks
// TODO (2) Create a public static constant String called ACTION_INCREMENT_WATER_COUNT
// TODO (6) Create a public static void method called executeTas
// TODO (7) Add a Context called context and String parameter called action to the parameter list
// TODO (8) If the action equals ACTION_INCREMENT_WATER_COUNT, call this class's incrementWaterCount
// TODO (3) Create a private static void method called incrementWaterCount
// TODO (4) Add a Context called context to the argument list
// TODO (5) From incrementWaterCount, call the PreferenceUtility method that will ultimately update the water count
public class ReminderTasks {
    public static final String ACTION_INCREMENT_WATER_COUNT="increment-water-count";
    //  TODO (2) Add a public static constant called ACTION_DISMISS_NOTIFICATION
    public static final String ACTION_DISMISS_NOTIFICATION="dismiss-notification";
    //add one more task for charging reminder
    public static final String ACTION_CHARGING_REMINDER="charging-reminder";
    public static void executeTask(Context context, String action)
    {
        if(action.equals(ACTION_INCREMENT_WATER_COUNT))
            IncrementWaterCount(context);
            //      TODO (3) If the user ignored the reminder, clear the notification
        else if(action.equals(ACTION_DISMISS_NOTIFICATION))
            NotificationUtils.clearAllNotifications(context);
        else if(action.equals(ACTION_CHARGING_REMINDER))
            issueChargingReminder(context);
    }
    // TODO (2) Create an additional task for issuing a charging reminder notification.
    // This should be done in a similar way to how you have an action for incrementingWaterCount
    // and dismissing notifications. This task should both create a notification AND
    // increment the charging reminder count (hint: there is a method for this in PreferenceUtilities)
    // When finished, you should be able to call executeTask with the correct parameters to execute
    // this task. Don't forget to add the code to executeTask which actually calls your new task!
    private static void issueChargingReminder(Context context)
    {
        PreferenceUtilities.incrementChargingReminderCount(context);
        NotificationUtils.remindUserBecauseCharging(context);
    }
    private static void IncrementWaterCount(Context context)
    {
        PreferenceUtilities.incrementWaterCount(context);
        //      TODO (4) If the water count was incremented, clear any notifications
        NotificationUtils.clearAllNotifications(context);
    }
}
