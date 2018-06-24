package com.example.eldho.intentservicesample;

import android.content.Intent;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class FirebaseJobService extends JobService {

    /**
     * Execution of tasks it happens in the main thread so move to background uses Async Task
     * Add FirebaseJobService into Manifest
     */
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                //Pro tip: I this its not necessary to run intent service in a thread because its already have an worker thread
                Intent jobServiceIntenet = new Intent(getApplicationContext(), MyIntentService.class);
                jobServiceIntenet.setAction(Values.JOB_REMINDER);
                startService(jobServiceIntenet);
                return null;
            }

            //Job finishes when the async task is finished, it signals by calling onPostExecute method
            //By calling JobFinished , Job Service tells the system when job is finished
            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);//Job parameters are a bundle of key value arguments that are passed in when job starts
                //needsReschedule -> False because the job is finished
            }
        };
        mBackgroundTask.execute();
        return true; // signals that our job is still doing some works
    }

    //Triggers when the job is stopped. eg:The task is to download file while in wifi and wifi gets disconnected "onStopJob" method is called
    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) {
            mBackgroundTask.cancel(true);
        }
        return true; // we have to retry the job when the conditions are met eg: get connection back to wifi
    }
}
