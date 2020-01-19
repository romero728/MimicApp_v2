package com.drm.mimicapp_v20;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    SharedPreferences spSettingsGame;
    SharedPreferences.Editor editorSettingsGame;

    private List<String> listTeams, listPoints;

    private Button btnBack;
    private TextView tvTeams, tvPoints;

    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        options = ActivityOptions.makeSceneTransitionAnimation(ScoreActivity.this);

        spSettingsGame = getSharedPreferences("settingsNewGame", Context.MODE_PRIVATE);

        listTeams = getIntent().getStringArrayListExtra("listTeams");
        listPoints = getIntent().getStringArrayListExtra("listPoints");

        btnBack = findViewById(R.id.btnBack);
        tvTeams = findViewById(R.id.tvSTeams);
        tvPoints = findViewById(R.id.tvSScoreTeam);

        tvTeams.setText(orderTeams());
        tvPoints.setText(orderPoints());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain();
            }
        });
    }

    private String orderTeams() {
        StringBuilder stringBuffer = new StringBuilder();

        for (int i = 0; i < listTeams.size(); i++) {
            stringBuffer.append(listTeams.get(i));
            stringBuffer.append("\n");
        }

        return stringBuffer.toString();
    }

    private String orderPoints() {
        StringBuilder stringBuffer = new StringBuilder();

        for (int i = 0; i < listPoints.size(); i++) {
            stringBuffer.append(listPoints.get(i));
            stringBuffer.append("\n");
        }

        return stringBuffer.toString();
    }

    private void goToMain() {
        editorSettingsGame = spSettingsGame.edit();
        editorSettingsGame.clear();
        editorSettingsGame.apply();

        Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {
        goToMain();
    }
}
