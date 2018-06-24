package com.example.eldho.intentservicesample;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * This class holds a method which schedules the job
 */
public class FirebaseJobDispatcherClass {
    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = (int) TimeUnit.MINUTES.toSeconds(1); // Window 1 mins

    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";  // unique tag to identify the job

    private static boolean sInitialized; //to keep track of whether the job is started or not

    /**
     * Method which starts the job
     * synchronized -> no need to execute more than once in a time
     */
    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {
        if (sInitialized) return; // checks if the job is already been setup, if true return

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(FirebaseJobService.class) //JobService class
                .setTag(REMINDER_JOB_TAG) //Set the UNIQUE tag used to identify this Job.
                /**Network constraints on which this Job should run.
                 * There are of multiple constraints available
                 *In this app, we're using the device charging constraint so that the job only executes if the device is charging.*/
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER) //Job "forever" or to have it die the next time the device boots up.
                .setRecurring(true) // If a Job with the tag with provided already exists, this new job will replace the old one.
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))   // creates a window the start window must be less than windowEnd
                .build();
        dispatcher.schedule(constraintReminderJob); // schedule the job
        sInitialized = true; // prevents re setup job if uts already running
    }
}
