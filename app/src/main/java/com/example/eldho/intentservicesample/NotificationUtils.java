package com.example.eldho.intentservicesample;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

public class NotificationUtils {
    public static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    public static final int WATER_REMINDER_NOTIFICATION_ID = 1234;
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;
    private static final int ACTION_INCREMENT_PENDING_INTENT_ID_ONE = 1;
    private static final int ACTION_INCREMENT_PENDING_INTENT_ID_TWO = 2;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    //Allows to relaunch the app when we click the notification
    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                //FLAG_UPDATE_CURRENT -> keeps this instance valid and just updates the extra data associated with it coming from new intent
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //Helps to create a bitmap image shown in the Notification
    private static Bitmap largeIcon(Context context) {
        /**Decode an image from the resources to an bitmap image*/
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.android);
        return largeIcon;
    }

    //This method is responsible for creating the notification and notification channel in which the notification belongs to and displaying it
    public static void createNotifications(Context context) {
        /**Get reference to notification manager*/
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /**From Oreo we need to display notifications in the notification channel*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID, //String ID
                    context.getString(R.string.main_notification_channel_name), //Name for the channel
                    NotificationManager.IMPORTANCE_HIGH); //Importance for the notification , In high we get headsUp notification
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.android)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title)) // Title
                .setContentText(context.getString(R.string.charging_reminder_notification_body)) //Text
                // check different styles ref: https://developer.android.com/training/notify-user/expanded
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE) // needed to add vibration permission on the manifest
                .setContentIntent(contentIntent(context)) //pending Intent (check its fn)

                //Adding the action buttons
                .addAction(incrementOne(context))
                .addAction(incrementTwo(context))
                .addAction(ignoreReminderAction(context))
                //Adding the action buttons ends here

                .setAutoCancel(true); //Notification will go away when user clicks on it
        /**this will give heads up notification on versions below Oreo*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        //WATER_REMINDER_NOTIFICATION_ID -> this ID can be used if the notification have to ba cancelled
        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Dismiss any notification which comes in
     */
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * For Increment 1! notification button
     */
    private static NotificationCompat.Action incrementOne(Context context) {
        // intent service is to relaunch the app and execute the task in background
        Intent incrementWaterCountIntent = new Intent(context, MyIntentService.class);
        incrementWaterCountIntent.setAction(Values.KEY_COUNT_1); // passing the value to be exe in the background
        //Wrapping intent with pending intent
        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_INCREMENT_PENDING_INTENT_ID_ONE,
                incrementWaterCountIntent, // intent service
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action drinkWaterAction = new NotificationCompat.Action(R.drawable.android,
                "Increment One",
                incrementWaterPendingIntent);
        return drinkWaterAction;
    }

    /**
     * For Increment 1! notification button
     */
    private static NotificationCompat.Action incrementTwo(Context context) {
        // intent service is to relaunch the app and execute the task in background
        Intent incrementWaterCountIntent = new Intent(context, MyIntentService.class);
        incrementWaterCountIntent.setAction(Values.KEY_COUNT_2); // passing the value to be exe in the background
        //Wrapping intent with pending intent
        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_INCREMENT_PENDING_INTENT_ID_TWO,
                incrementWaterCountIntent, // intent service
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action drinkWaterAction = new NotificationCompat.Action(R.drawable.android,
                "Increment One",
                incrementWaterPendingIntent);
        return drinkWaterAction;
    }

    /**
     * For dismissing the notification notification button
     */
    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        // intent service is to relaunch the app and execute the task in background
        Intent ignoreReminderIntent = new Intent(context, MyIntentService.class);
        ignoreReminderIntent.setAction(Values.IGNORE_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_cancel_black_24dp,
                "No, thanks.",
                ignoreReminderPendingIntent);
        return ignoreReminderAction;
    }
}
