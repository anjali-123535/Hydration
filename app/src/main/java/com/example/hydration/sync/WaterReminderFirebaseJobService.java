package com.example.hydration.sync;

//import android.app.job.JobParameters;
//import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;



import com.firebase.jobdispatcher.Job;

import com.firebase.jobdispatcher.JobParameters;

import com.firebase.jobdispatcher.JobService;

import com.firebase.jobdispatcher.RetryStrategy;
// TODO (3) WaterReminderFirebaseJobService should extend from JobService
public class WaterReminderFirebaseJobService extends JobService {
    private AsyncTask mBackgroundTask;

    // TODO (4) Override onStartJob
    //just like services jobservice also runs on the main thread so
    // TODO (5) By default, jobs are executed on the main thread, so make an anonymous class extending
    //  AsyncTask called mBackgroundTask.
    // TODO (6) Override doInBackground
    // TODO (7) Use ReminderTasks to execute the new charging reminder task you made, use
    // this service as the context (WaterReminderFirebaseJobService.this) and return null
    // when finished.
    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mBackgroundTask=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context=WaterReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context,ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }
            // TODO (8) Override onPostExecute and call jobFinished. Pass the job parameters
            // and false to jobFinished. This will inform the JobManager that your job is done
            // and that you do not want to reschedule the job.
            @Override
            protected void onPostExecute(Object o) {
                /*
                 * Once the AsyncTask is finished, the job is finished. To inform JobManager that
                 * you're done, you call jobFinished with the jobParamters that were passed to your
                 * job and a boolean representing whether the job needs to be rescheduled. This is
                 * usually if something didn't work and you want the job to try running again.
                 */
                super.onPostExecute(o);
                jobFinished(jobParameters,false);//false so that no need to rescheduele the job
            }
        };
        // TODO (9) Execute the AsyncTask
        // TODO (10) Return true

        mBackgroundTask.execute(jobParameters);
        return true;
        //true in order to indicate that our job is still doing work,remember this is on  a different thread
    }
    // TODO (11) Override onStopJo
    // TODO (12) If mBackgroundTask is valid, cancel it
    // TODO (13) Return true to signify the job should be retried
//it gets called when the requirements of teh job are no longer met
    //suppose you are constrained to download only over wifi and in the middle suddenly your wifi gets shut-oof then onStopJob would be called


    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
      most likely because the runtime constraints associated with the job are no longer satisfied.*
     * @return whether the job should be retried
     * @see //Job.Builder#setRetryStrategy(RetryStrategy)
     * @see //RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mBackgroundTask!=null)
            mBackgroundTask.cancel(true);
        return true;
        //true because we want the task to get reumed whenever the conditions are re-met
    }
}
