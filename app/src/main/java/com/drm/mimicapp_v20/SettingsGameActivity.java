package com.drm.mimicapp_v20;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.drm.mimicapp_v20.db_connection.GetWords;

public class SettingsGameActivity extends AppCompatActivity {
    Dialog popupHelp;

    String roundsSelected, timeSelected;

    Spinner sTime = null;
    Spinner sRounds = null;

    SharedPreferences spSettingsGame, spUserData;
    SharedPreferences.Editor editorSettingsGame;

    ActivityOptions options;

    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_game);
        setToolbar();

        popupHelp = new Dialog(this);
        ImageButton ibHelp = findViewById(R.id.ibSGHelp);

        options = ActivityOptions.makeSceneTransitionAnimation(SettingsGameActivity.this);

        progressDialog = new ProgressDialog(this);

        ibHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp.setContentView(R.layout.custompopuphelp);
                ImageView ivClose = popupHelp.findViewById(R.id.imageView);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupHelp.dismiss();
                    }
                });

                popupHelp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupHelp.show();
            }
        });

        spSettingsGame = getSharedPreferences("settingsNewGame", Context.MODE_PRIVATE);
        spUserData = getSharedPreferences("userData", Context.MODE_PRIVATE);

        final TextView tvLabel3 = findViewById(R.id.tvSGLabel2);
        final TextView tvLabel4 = findViewById(R.id.tvSGLabel3);

        sTime = findViewById(R.id.sSGTime);
        sRounds = findViewById(R.id.sSGRounds);

        Button btnNext = findViewById(R.id.btnSGBeginPlay);

        ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(this,
                R.array.time_options, android.R.layout.simple_spinner_item);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTime.setAdapter(adapterTime);

        sTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                timeSelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapterRounds = ArrayAdapter.createFromResource(this,
                R.array.rounds_options, android.R.layout.simple_spinner_item);
        adapterRounds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRounds.setAdapter(adapterRounds);

        sRounds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roundsSelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGame();
            }
        });
    }

    public void goToGame() {
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        editorSettingsGame = spSettingsGame.edit();
        editorSettingsGame.putString("timeKey", timeSelected);
        editorSettingsGame.putString("roundsKey", roundsSelected);
        editorSettingsGame.apply();

        if (!spUserData.getString("userIdKey", null).isEmpty()) {
            new GetWords(this).execute(spUserData.getString("urlKey", null),
                    spUserData.getString("userIdKey", null),
                    spSettingsGame.getString("kindCategoryKey", null),
                    spSettingsGame.getString("categoryKey", null));
        } else {
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        progressDialog.dismiss();

        super.onStop();
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
