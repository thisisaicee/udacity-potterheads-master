/*
    Copyright 2019 Ifeanyichukwu Onyeabor

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package net.aicee.potterheads.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.orhanobut.logger.Logger;

public class PotterHeadsFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> fetchArticleAsyncTask;


    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        fetchArticleAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                PotterHeadsTask.syncArticles(context);
                Logger.d("Sync Job Started");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Logger.d("Sync Job Finished");
                jobFinished(jobParameters, false);
            }
        };


        fetchArticleAsyncTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (fetchArticleAsyncTask != null) {
            fetchArticleAsyncTask.cancel(true);
        }
        return true;
    }
}