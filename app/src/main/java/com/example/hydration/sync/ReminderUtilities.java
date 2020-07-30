package com.example.hydration.sync;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

public class ReminderUtilities {
    // TODO (15) Create three constants and one variable:
    //  - REMINDER_INTERVAL_SECONDS should be an integer constant storing the number of seconds in 15 minutes
    public static  int REMINDER_INTERVAL_SECONDS = 3600;
    //  - SYNC_FLEXTIME_SECONDS should also be an integer constant storing the number of seconds in 15 minutes
    private static final int SYNC_FLEXTIME_SECONDS = (int) TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_SECONDS);
    //  - REMINDER_JOB_TAG should be a String constant, storing something like "hydration_reminder_tag"
    private static final String REMINDER_JOB_TAG = "hydration-reminder-tag";
    //  - sInitialized should be a private static boolean variable which will store whether the job
    private static boolean sInitialized;
    //    has been activated or not

    // TODO (16) Create a synchronized, public static method called scheduleChargingReminder that takes
    synchronized public static void scheduleChargingReminder(Context context) {
        // in a context. This method will use FirebaseJobDispatcher to schedule a job that repeats roughly
        // every REMINDER_INTERVAL_SECONDS when the phone is charging. It will trigger WaterReminderFirebaseJobService
        // Checkout https://github.com/firebase/firebase-jobdispatcher-android for an example

        // TODO (17) If the job has already been initialized, return
        if(sInitialized)
             return;
        GooglePlayDriver googlePlayDriver=new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher=new FirebaseJobDispatcher(googlePlayDriver);
        // TODO (18) Create a new GooglePlayDriver

        // TODO (19) Create a new FirebaseJobDispatcher with the driver
                               Job constraintReminderJob= firebaseJobDispatcher.newJobBuilder()
                                       .setService(WaterReminderFirebaseJobService.class)
                                       .setTag(REMINDER_JOB_TAG)
                                       .setConstraints(Constraint.DEVICE_CHARGING)
                                       .setLifetime(Lifetime.FOREVER)
                                       .setRecurring(true)
                                       .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS,REMINDER_INTERVAL_SECONDS+SYNC_FLEXTIME_SECONDS))
                                       .setReplaceCurrent(true)
                                       .build();
        // TODO (20) Use FirebaseJobDispatcher's newJobBuilder method to build a job which:
        // - has WaterReminderFirebaseJobService as it's service
        // - has the tag REMINDER_JOB_TA
        // - only triggers if the device is charging
        // - has the lifetime of the job as forever
        // - has the job recur
        // - occurs every 15 minutes with a window of 15 minutes. You can do this using a
        //   setTrigger, pasing in a Trigger.executionWindow
        // - replaces the current job if it's already running
        // Finally, you should build the job.
        // TODO (21) Use dispatcher's schedule method to schedule the job
        firebaseJobDispatcher.schedule(constraintReminderJob);
        // TODO (22) Set sInitialized to true to mark that we're done setting up the job

    sInitialized=true;
    }
}