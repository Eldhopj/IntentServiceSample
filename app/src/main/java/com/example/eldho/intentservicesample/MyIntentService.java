package com.example.eldho.intentservicesample;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import static com.example.eldho.intentservicesample.Values.clickCountBtn1;
import static com.example.eldho.intentservicesample.Values.clickCountBtn2;

/**
 * According to the string which get passed into the action we increment the associated counter values
 * pass the incremented values as parameters for the saving into shared pref fns
 */


public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    public static final String ACTION_FOO = Values.KEY_COUNT_1;
    public static final String ACTION_BAZ = Values.KEY_COUNT_2;
    public static final String ACTION_IGNORE_NOTIFICATION = Values.IGNORE_NOTIFICATION;
    public static final String ACTION_JOB_REMINDER = Values.JOB_REMINDER;

    public MyIntentService() {

        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction(); // gets string here
            if (ACTION_FOO.equals(action)) {
                clickCountBtn1 += 1;
                saveIntoSharedPrefs1(clickCountBtn1);
                //can add clear notification fn here if necessary
            } else if (ACTION_BAZ.equals(action)) {
                clickCountBtn2 += 1;
                saveIntoSharedPrefs2(clickCountBtn2);
            } else if (ACTION_IGNORE_NOTIFICATION.equals(action)) {
                NotificationUtils.clearAllNotifications(getApplicationContext()); // to clear the notification fn
            }
            //Job service
            else if (ACTION_JOB_REMINDER.equals(action)) {
                //Getting value from shared prefs
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                Values.jobCalls = pref.getInt("jobCount", 0);
                //Getting value from shared prefs ends here
                Values.jobCalls += 1;
                saveIntoJobReminderSharedPrefs(Values.jobCalls);

                NotificationUtils.createNotifications(getApplicationContext()); // Show notification
            }

        }
    }


    private void saveIntoSharedPrefs1(int clicks) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("keyCountOne", clicks);
        Toast.makeText(this, Integer.toString(clicks), Toast.LENGTH_SHORT).show();
        editor.apply();
    }

    private void saveIntoJobReminderSharedPrefs(int clicks) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("jobCount", clicks);
        Toast.makeText(this, Integer.toString(clicks), Toast.LENGTH_SHORT).show();
        editor.apply();
    }


    private void saveIntoSharedPrefs2(int clicks) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("keyCountTwo", clicks);
        editor.apply();
    }
}
