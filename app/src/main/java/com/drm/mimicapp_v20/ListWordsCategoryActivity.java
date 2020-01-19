package com.drm.mimicapp_v20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.drm.mimicapp_v20.db_connection.AddWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ListWordsCategoryActivity extends AppCompatActivity {
    private EditText etAddWord;

    private String category = "", answerAdd;

    SharedPreferences spSeeCategory;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_words_category);
        setToolbar();

        spSeeCategory = getSharedPreferences("seeCategory", Context.MODE_PRIVATE);
        String kind = spSeeCategory.getString("kindCategoryKey", null);
        category = spSeeCategory.getString("categoryKey", null);
        answerAdd = getIntent().getStringExtra("resultAddWord");
        String words;

        if (getIntent().getStringExtra("categoryWords") == null) {
            words = spSeeCategory.getString("categoryWords", null);
        } else {
            words = getIntent().getStringExtra("categoryWords");

            editor = spSeeCategory.edit();
            editor.putString("categoryWords", words);
            editor.apply();
        }

        if (answerAdd != null) {
            if (answerAdd.equals("success")) {
                Toast.makeText(this, "La palabra se ha añadido con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error añadiendo la palabra, por favor intenta más tarde", Toast.LENGTH_SHORT).show();
            }
        }

        etAddWord = findViewById(R.id.etLWAddWord);
        Button btnAddWord = findViewById(R.id.btnAddWord);
        ListView lvWords = findViewById(R.id.lvLWWords);
        TextView tvCategory = findViewById(R.id.tvLWCategory);

        if (kind.equals("general")) {
            etAddWord.setVisibility(View.GONE);
            btnAddWord.setVisibility(View.GONE);
        } else {
            etAddWord.setVisibility(View.VISIBLE);
            btnAddWord.setVisibility(View.VISIBLE);
        }

        tvCategory.setText(category);

        List<String> listWords = getWords(words);
        listWords.remove(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listWords);

        lvWords.setAdapter(adapter);

        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWord();
            }
        });
    }

    private void saveWord() {
        String word = etAddWord.getText().toString();

        if (!word.isEmpty()) {
            loading();

            editor = spSeeCategory.edit();
            editor.putString("currentWord", word);
            editor.apply();

            new AddWord(this).execute(spSeeCategory.getString("urlKey", null), category, word);
        }
    }

    private List<String> getWords(String w) {
        if (answerAdd != null && answerAdd.equals("success")) {
            w += "|" + spSeeCategory.getString("currentWord", null);
        }

        String[] splitWords = w.split(Pattern.quote("|"));

        return new ArrayList<>(Arrays.asList(splitWords));
    }

    private void loading() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    public void onBackPressed() {

    }
}
