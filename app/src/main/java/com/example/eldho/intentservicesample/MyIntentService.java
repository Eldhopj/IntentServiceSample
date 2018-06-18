package com.example.eldho.intentservicesample;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import static com.example.eldho.intentservicesample.Values.clickCountBtn1;
import static com.example.eldho.intentservicesample.Values.clickCountBtn2;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    public static final String ACTION_FOO = Values.KEY_COUNT_1;
    public static final String ACTION_BAZ = Values.KEY_COUNT_2;


    public MyIntentService() {

        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                clickCountBtn1 += 1;
               handleActionFoo(clickCountBtn1);
            } else if (ACTION_BAZ.equals(action)) {
                clickCountBtn2 += 1;
                handleActionBaz(clickCountBtn2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    private void handleActionFoo(int clicks) {
        // TODO: Handle action Foo
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("keyCountOne", clicks);
        Toast.makeText(this, Integer.toString(clicks), Toast.LENGTH_SHORT).show();
        editor.apply();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */

    private void handleActionBaz(int clicks) {
        // TODO: Handle action Baz
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("keyCountTwo", clicks);
        editor.apply();
    }
}
