package com.drm.mimicapp_v20;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ChooseCategoryActivity extends AppCompatActivity {
    SharedPreferences spSettingsGame;
    SharedPreferences.Editor editorChooseCategory;

    ConstraintLayout.LayoutParams params;

    List<String> listCategories;

    String categorySelected, kindCategorySelected;

    Spinner sKindCategory = null;
    Spinner sCategory = null;

    ActivityOptions options;

    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        setToolbar();

        spSettingsGame = getSharedPreferences("settingsNewGame", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);

        final TextView tvLabel3 = findViewById(R.id.tvCCLabel3);
        sKindCategory = findViewById(R.id.sCCKindCategory);
        sCategory = findViewById(R.id.sCCCategory);

        Button btnNext = findViewById(R.id.btnCCNext);

        options = ActivityOptions.makeSceneTransitionAnimation(ChooseCategoryActivity.this);

        /*--- Obtener categorías */

        String splitKind = Pattern.quote("/");
        String splitCategory = Pattern.quote("|");
        String listCategories = getIntent().getExtras().getString("listCategories");
        String[] allCategories = listCategories.split(splitKind);
        final String[] generalCategories = allCategories[0].split(splitCategory);
        String[] customCategories = new String[1];
        generalCategories[0] = "Todas";

        if (allCategories.length > 1) {
            customCategories = allCategories[1].split(splitCategory);
            customCategories[0] = "Todas";
        } else {
            customCategories[0] = "No has creado categorías";
        }

        final String[] finalCustomCategories = customCategories;

        /* --- */

        ArrayAdapter<CharSequence> adapterKind = ArrayAdapter.createFromResource(this,
                R.array.kind_categories, android.R.layout.simple_spinner_item);
        adapterKind.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKindCategory.setAdapter(adapterKind);

        sKindCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        kindCategorySelected = "all";
                        hideCategories(tvLabel3, sCategory);
                        break;
                    case 1:
                        kindCategorySelected = "general";
                        getCategories(generalCategories);
                        showCategories(tvLabel3, sCategory);
                        break;
                    case 2:
                        kindCategorySelected = "custom";
                        getCategories(finalCustomCategories);
                        showCategories(tvLabel3, sCategory);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!kindCategorySelected.equals("all")) {
                    if (categorySelected.equals("No has creado categorías")) {
                        Toast.makeText(ChooseCategoryActivity.this,
                                "Debes seleccionar una categoría válida", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        goToSettings();
                    }
                } else {
                    goToSettings();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        progressDialog.dismiss();

        super.onStop();
    }

    public void getCategories(String[] categories) {
        listCategories = new ArrayList<>(Arrays.asList(categories));

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listCategories);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(adapterCategory);
    }

    public void showCategories(TextView tv3, Spinner s) {
        tv3.setVisibility(View.VISIBLE);
        s.setVisibility(View.VISIBLE);
    }

    public void hideCategories(TextView tv3, Spinner s) {
        tv3.setVisibility(View.INVISIBLE);
        s.setVisibility(View.INVISIBLE);
    }

    public void goToSettings() {
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        editorChooseCategory = spSettingsGame.edit();
        editorChooseCategory.putString("kindCategoryKey", kindCategorySelected);
        editorChooseCategory.putString("categoryKey", categorySelected);
        editorChooseCategory.apply();

        Intent intent = new Intent(ChooseCategoryActivity.this,
                SettingsGameActivity.class);
        startActivity(intent, options.toBundle());

        finish();
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
