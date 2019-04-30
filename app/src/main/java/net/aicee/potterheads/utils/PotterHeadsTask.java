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

import com.orhanobut.logger.Logger;

import java.util.List;

import net.aicee.potterheads.data.AppDatabase;
import net.aicee.potterheads.data.Book;

public class PotterHeadsTask {

    synchronized public static void syncArticles(Context context) {

        List<Book> books = NetworkUtils.getArticles(context);
        if (books != null && books.size() > 0) {
            AppDatabase.getAppDatabase(context).articleDao().insert(books);
        }
        Logger.d("Sync Books");
    }

}