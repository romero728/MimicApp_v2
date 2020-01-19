package com.drm.mimicapp_v20;

import android.animation.LayoutTransition;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.drm.mimicapp_v20.db_connection.CreateCategory;

import java.util.ArrayList;

public class CreateCategoryActivity extends AppCompatActivity {
    ActivityOptions options;

    SharedPreferences sharedPreferences;

    Button btnAddWord, btnNext, btnSave;
    EditText etNameCategory, etAddWord;
    LinearLayout linAddWords, linGeneral;
    ListView lvWords;
    TextView tvLabel2, tvLabel3;

    ArrayList<String> arWords;
    String nameCategory, emailUser, words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        setToolbar();

        options = ActivityOptions.makeSceneTransitionAnimation(CreateCategoryActivity.this);

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        emailUser = sharedPreferences.getString("emailKey", null);

        arWords = new ArrayList<>();

        btnAddWord  = findViewById(R.id.btnAddWord);
        btnNext = findViewById(R.id.btnCCNext);
        btnSave = findViewById(R.id.btnCCSave);
        etNameCategory = findViewById(R.id.etCCCategoryName);
        etAddWord = findViewById(R.id.etCCAddWord);
        linAddWords = findViewById(R.id.linCCAddWord);
        linGeneral = findViewById(R.id.linCCGeneral);
        lvWords = findViewById(R.id.lvCCWords);
        tvLabel2 = findViewById(R.id.tvCCLabel2);
        tvLabel3 = findViewById(R.id.tvCCLabel3);

        linAddWords.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linGeneral.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etNameCategory.getText().toString().isEmpty()) {
                    nameCategory = etNameCategory.getText().toString();

                    btnNext.setVisibility(View.GONE);
                    tvLabel2.setVisibility(View.VISIBLE);
                    tvLabel3.setVisibility(View.VISIBLE);
                    linAddWords.setVisibility(View.VISIBLE);
                    lvWords.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);

                    etNameCategory.setFocusable(false);
                    etNameCategory.setEnabled(false);
                } else {
                    Toast.makeText(CreateCategoryActivity.this, "Debes ingresar el nombre de la" +
                            " categoría", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentWord = etAddWord.getText().toString();

                if (!currentWord.isEmpty()) {
                    addWord(currentWord);
                    etAddWord.setText("");
                } else {
                    Toast.makeText(CreateCategoryActivity.this, "Debes ingresar una palabra",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arWords.size() < 2) {
                    Toast.makeText(CreateCategoryActivity.this, "Debes ingresar al " +
                            "menos tres palabras", Toast.LENGTH_SHORT).show();
                } else {
                    loading();
                    words = convertWords();
                    saveCategory();
                }
            }
        });
    }

    private void addWord(String word) {
        arWords.add(word);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arWords);

        lvWords.setAdapter(adapter);
    }

    private String convertWords() {
        StringBuilder words = new StringBuilder();

        for (int i = 0; i < arWords.size(); i++) {
            words.append("|").append(arWords.get(i));
        }

        return words.toString();
    }

    private void saveCategory() {
        new CreateCategory(this).execute(sharedPreferences.getString("urlKey", null), emailUser, nameCategory, words);
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
