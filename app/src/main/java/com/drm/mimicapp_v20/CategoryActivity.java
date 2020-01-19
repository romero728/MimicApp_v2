package com.drm.mimicapp_v20;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.drm.mimicapp_v20.db_connection.CategoriesEdit;

public class CategoryActivity extends AppCompatActivity {
    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setToolbar();

        options = ActivityOptions.makeSceneTransitionAnimation(CategoryActivity.this);

        Button btnCreateCategories = findViewById(R.id.btnCreateCategories);
        Button btnSeeCategories = findViewById(R.id.btnSeeCategories);

        btnCreateCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateCategory();
            }
        });

        btnSeeCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListCategory();
            }
        });
    }

    public void goToCreateCategory() {
        loading();
        Intent intent = new Intent(this, CreateCategoryActivity.class);
        startActivity(intent);
    }

    public void goToListCategory() {
        loading();
        SharedPreferences spUser = getSharedPreferences("userData", Context.MODE_PRIVATE);
        String url = spUser.getString("urlKey", null);
        String userId = spUser.getString("userIdKey", null);

        new CategoriesEdit(this).execute(url, userId);
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
