package com.drm.mimicapp_v20.db_connection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.drm.mimicapp_v20.GameActivity;
import com.drm.mimicapp_v20.ListWordsCategoryActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class AddWord extends AsyncTask<String, String, String> {
    private static Context context;

    public AddWord(Context context) {
        AddWord.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String link = arg0[0];
            String categoryName = arg0[1];
            String word = arg0[2];

            link = link + "addWord.php";
            String parameters = URLEncoder.encode("categoryName", "UTF-8") + "=" +
                    URLEncoder.encode(categoryName, "UTF-8");
            parameters += "&" + URLEncoder.encode("word", "UTF-8") + "=" +
                    URLEncoder.encode(word, "UTF-8");

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
        Log.i("result", result);
        Intent intent = new Intent(context, ListWordsCategoryActivity.class);
        intent.putExtra("resultAddWord", result);
        context.startActivity(intent);
    }
}
