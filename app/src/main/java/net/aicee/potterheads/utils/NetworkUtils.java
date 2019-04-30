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
import android.net.ConnectivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.aicee.potterheads.data.Book;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkUtils {

    private static final String JSON_URL = "http://aicee.net/potterheads/potter-heads.json";

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static List<Book> getArticles(Context context) {
        if (!isNetworkAvailable(context)) {
            return new ArrayList<>();
        }

        List<Book> book = null;

        try {
            book = parseJSON(getJson(JSON_URL));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return book;
    }

    private static String getJson(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    private static List<Book> parseJSON(String jsonString) throws JSONException {
        JSONArray results = new JSONArray(jsonString);

        List<Book> bookList = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);

            Book article = new Book(object.getInt("id"),
                    object.getDouble("aspect_ratio"),
                    object.getString("thumb"),
                    object.getString("author"),
                    object.getString("photo"),
                    object.getString("title"),
                    object.getString("body"),
                    object.getString("published_date"));

            bookList.add(article);
        }

        return bookList;
    }
}
