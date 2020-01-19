package com.drm.mimicapp_v20;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences spSettings;
    SharedPreferences.Editor editor;

    ActivityOptions options;

    Switch switchSound, switchVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar();

        spSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        options = ActivityOptions.makeSceneTransitionAnimation(SettingsActivity.this);

        switchSound = findViewById(R.id.switchSound);
        switchVibrate = findViewById(R.id.switchVibrate);

        if (spSettings.getString("checkSound", null) != null && spSettings.getString("checkVibrate", null) != null) {
            if (spSettings.getString("checkSound", null).equals("1")) {
                switchSound.setChecked(true);
            } else {
                switchSound.setChecked(false);
            }

            if (spSettings.getString("checkVibrate", null).equals("1")) {
                switchVibrate.setChecked(true);
            } else {
                switchVibrate.setChecked(false);
            }
        } else {
            switchSound.setChecked(true);
            switchVibrate.setChecked(true);
        }

        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("switch", "sound: " + String.valueOf(b));
                saveSound(b);
            }
        });

        switchVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("switch", "vibrate: " + String.valueOf(b));
                saveVibrate(b);
            }
        });
    }

    private void saveSound(boolean b) {
        String choise = "";
        if (b) {
            choise = "1";
        } else {
            choise = "0";
        }

        editor = spSettings.edit();
        editor.putString("checkSound", choise);
        editor.apply();
    }

    private void saveVibrate(boolean b) {
        String choise = "";
        if (b) {
            choise = "1";
        } else {
            choise = "0";
        }

        editor = spSettings.edit();
        editor.putString("checkVibrate", choise);
        editor.apply();
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
