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

package net.aicee.potterheads.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import net.aicee.potterheads.data.AppDatabase;
import net.aicee.potterheads.data.Article;


public class BookLoader extends AsyncTaskLoader<Article> {
    private Article taskData = null;
    private int ID;

    public BookLoader(@NonNull Context context, int id) {
        super(context);
        ID = id;
    }

    @Override
    protected void onStartLoading() {
        if (taskData != null) {
            deliverResult(taskData);
        } else {
            forceLoad();
        }
    }

    @Override
    public Article loadInBackground() {
        try {
            return AppDatabase.getAppDatabase(getContext()).articleDao().findById(ID);

        } catch (Exception e) {
            Logger.e("Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    public void deliverResult(Article data) {
        taskData = data;
        super.deliverResult(data);
    }
}
