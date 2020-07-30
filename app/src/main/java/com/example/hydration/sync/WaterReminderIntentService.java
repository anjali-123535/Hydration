package com.example.hydration.sync;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
/**

 * An {@link IntentService} subclass for handling asynchronous task requests in

 * a service on a separate handler thread.

 */
// TODO (9) Create WaterReminderIntentService and extend it from IntentService
//  TODO (10) Create a default constructor that calls super with the name of this clas
//  TODO (11) Override onHandleIntent
//      TODO (12) Get the action from the Intent that started this Service
//      TODO (13) Call ReminderTasks.executeTask and pass in the action to be performed
public class WaterReminderIntentService extends IntentService {

    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action=intent.getAction();
        //if(action.equals(ReminderTasks.ACTION_INCREMENT_WATER_COUNT))
            ReminderTasks.executeTask(this,action);
    }
}
