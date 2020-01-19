package com.drm.mimicapp_v20;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.drm.mimicapp_v20.db_connection.GetWordsCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ListCategoryActivity extends AppCompatActivity {
    String categorySelected, kindCategory;

    Spinner sKindCategory = null;
    Spinner sCategorySelected = null;

    List<String> listCategories;

    SharedPreferences spUserData, spSeeCategory;
    SharedPreferences.Editor editor;

    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        setToolbar();

        options = ActivityOptions.makeSceneTransitionAnimation(ListCategoryActivity.this);

        spUserData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        spSeeCategory = getSharedPreferences("seeCategory", Context.MODE_PRIVATE);

        editor = spSeeCategory.edit();
        editor.putString("urlKey", spUserData.getString("urlKey", null));
        editor.putString("userIdKey", spUserData.getString("userIdKey", null));
        editor.apply();

        sKindCategory = findViewById(R.id.sLCKindCategory);
        sCategorySelected = findViewById(R.id.sLCCategory);

        Button btnNext = findViewById(R.id.btnLCGoCategory);

        /*--- Obtener categorías */

        String splitKind = Pattern.quote("/");
        String splitCategory = Pattern.quote("|");
        String listCategories;

        if (getIntent().getStringExtra("listCategories") == null) {
            listCategories = spSeeCategory.getString("listCategoriesKey", null);
        } else {
            listCategories = getIntent().getStringExtra("listCategories");

            editor = spSeeCategory.edit();
            editor.putString("listCategoriesKey", listCategories);
            editor.apply();
        }

        String[] allCategories = listCategories.split(splitKind);
        final String[] generalCategories = allCategories[0].split(splitCategory);
        String[] customCategories = new String[1];
        generalCategories[0]= "-------";

        if (allCategories.length > 1) {
            customCategories = allCategories[1].split(splitCategory);
            customCategories[0] = "-------";
        } else {
            customCategories[0] = "No has creado categorías";
        }

        final String[] finalCustomCategories = customCategories;

        /* --- */

        ArrayAdapter<CharSequence> adapterKind = ArrayAdapter.createFromResource(this,
                R.array.kind_list_categories, android.R.layout.simple_spinner_item);
        adapterKind.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKindCategory.setAdapter(adapterKind);

        sKindCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        kindCategory = "general";
                        getCategories(generalCategories);
                        break;
                    case 1:
                        kindCategory = "custom";
                        getCategories(finalCustomCategories);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sCategorySelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                if (categorySelected.equals("-------") || categorySelected.equals("No has creado categorías")) {
                    Toast.makeText(ListCategoryActivity.this, "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
                } else {
                    goToCategoryWords();
                }
            }
        });
    }

    private void getCategories(String[] categories) {
        listCategories = new ArrayList<>(Arrays.asList(categories));

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listCategories);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategorySelected.setAdapter(adapterCategory);
    }

    private void goToCategoryWords() {
        int custom;

        if (kindCategory.equals("general")) {
            custom = 0;
        } else {
            custom = 1;
        }

        editor = spSeeCategory.edit();
        editor.putString("customKey", String.valueOf(custom));
        editor.putString("kindCategoryKey", kindCategory);
        editor.putString("categoryKey", categorySelected);
        editor.apply();

        loading();
        new GetWordsCategory(this).execute(spSeeCategory.getString("urlKey", null),
                spSeeCategory.getString("userIdKey", null),
                spSeeCategory.getString("kindCategoryKey", null),
                spSeeCategory.getString("categoryKey", null));

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
