package com.drm.mimicapp_v20.db_connection;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.drm.mimicapp_v20.ChooseCategoryActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ListCategories extends AsyncTask<String, String, String> {
    private static Context context;

    public ListCategories(Context context) {
        ListCategories.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String link = arg0[0];
            String userId = arg0[1];

            link = link + "listCategories.php";
            String parameters = URLEncoder.encode("userId", "UTF-8") + "=" +
                    URLEncoder.encode(userId, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(parameters);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
        Intent intent = new Intent(context, ChooseCategoryActivity.class);
        intent.putExtra("listCategories", result);
        context.startActivity(intent, options.toBundle());
    }
}
