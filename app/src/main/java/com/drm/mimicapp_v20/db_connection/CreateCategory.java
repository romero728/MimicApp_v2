package com.drm.mimicapp_v20.db_connection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.drm.mimicapp_v20.CategoryActivity;
import com.drm.mimicapp_v20.CreateCategoryActivity;
import com.drm.mimicapp_v20.ListWordsCategoryActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class CreateCategory extends AsyncTask<String, String, String> {
    private static Context context;

    public CreateCategory(Context context) {
        CreateCategory.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String link = arg0[0];
            String emailUser = arg0[1];
            String nameCategory = arg0[2];
            String words = arg0[3];

            link = link + "createCategory.php";
            String parameters = URLEncoder.encode("emailUser", "UTF-8") + "=" +
                    URLEncoder.encode(emailUser, "UTF-8");
            parameters += "&" + URLEncoder.encode("nameCategory", "UTF-8") + "=" +
                    URLEncoder.encode(nameCategory, "UTF-8");
            parameters += "&" + URLEncoder.encode("words", "UTF-8") + "=" +
                    URLEncoder.encode(words, "UTF-8");

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

        if (result.equals("success")) {
            Toast.makeText(context, "Categoría creada con éxito", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, CategoryActivity.class);
            //intent.putExtra("resultAddWord", result);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Hubo un error con la categoría", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, CreateCategoryActivity.class);
            context.startActivity(intent);
        }
    }
}
