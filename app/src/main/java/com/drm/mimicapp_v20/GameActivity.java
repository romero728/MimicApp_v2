package com.drm.mimicapp_v20;

import android.animation.LayoutTransition;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class GameActivity extends AppCompatActivity {
    SharedPreferences spSettingsGame, spSettings;
    SharedPreferences.Editor editorSettingsGame;
    private Vibrator vibrator;
    private MediaPlayer mp;

    private boolean checkSound, checkVibrate;
    private int currentRound = 1, maxRounds, currentTeam = 0;

    private long timeRound, timeLeft;
    private List<String> listTeams, listWords, listPointsString;
    private List<String>listCurrentWords = new ArrayList<>();
    private List<Integer> listPoints = new ArrayList<>();

    private Button btnPass, btnCorrect, btnContinue;
    private TextView tvCurrentTeam, tvCurrentRound, tvCurrentWord, tvGTime;

    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setToolbar();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        LinearLayout linGameTeam = findViewById(R.id.linGameTeam);
        LinearLayout linGameRound = findViewById(R.id.linGameRound);
        LinearLayout linGameTime = findViewById(R.id.linGameTime);
        LinearLayout linGameWord = findViewById(R.id.linGameWord);
        LinearLayout linGameButtons = findViewById(R.id.linGameButtons);

        linGameTeam.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linGameRound.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linGameTime.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linGameWord.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linGameButtons.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        options = ActivityOptions.makeSceneTransitionAnimation(GameActivity.this);

        btnPass = findViewById(R.id.btnPass);
        btnCorrect = findViewById(R.id.btnCorrect);
        btnContinue = findViewById(R.id.btnContinue);
        tvCurrentTeam = findViewById(R.id.tvCurrentTeam);
        tvCurrentRound = findViewById(R.id.tvCurrentRound);
        tvCurrentWord = findViewById(R.id.tvCurrentWord);
        tvGTime = findViewById(R.id.tvGTime);

        tvCurrentWord.setVisibility(View.INVISIBLE);
        String stateBegin = "Iniciar juego";
        btnContinue.setText(stateBegin);
        btnContinue.setVisibility(View.VISIBLE);
        btnCorrect.setVisibility(View.GONE);
        btnPass.setVisibility(View.GONE);

        spSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        spSettingsGame = getSharedPreferences("settingsNewGame", Context.MODE_PRIVATE);

        if (spSettings.getString("checkSound", null) != null && spSettings.getString("checkVibrate", null) != null) {
            if (spSettings.getString("checkSound", null).equals("1")) {
                checkSound = true;
            } else {
                checkSound = false;
            }

            if (spSettings.getString("checkVibrate", null).equals("1")) {
                checkVibrate = true;
            } else {
                checkVibrate = false;
            }
        }

        String wordsp, teamsp, roundsp, timerousp;

        wordsp = getIntent().getExtras().getString("gameWords");
        teamsp = spSettingsGame.getString("numberTeamsKey", null);
        roundsp = spSettingsGame.getString("roundsKey", null);
        timerousp = spSettingsGame.getString("timeKey", null);

        listWords = getWords(wordsp);
        listWords.remove(0);
        listCurrentWords.addAll(listWords);
        listTeams = getTeams(teamsp);
        maxRounds = getRounds(roundsp);
        timeRound = getTimeRound(timerousp);
        timeLeft = timeRound;

        String ct = listTeams.get(currentTeam);
        tvCurrentTeam.setText(ct);

        tvCurrentWord.setText(getRandomWord());

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSound) {
                    mp = MediaPlayer.create(GameActivity.this, R.raw.error);
                    mp.start();
                }

                if (checkVibrate) {
                    vibrationChoice();
                }

                tvCurrentWord.setText(getRandomWord());
            }
        });

        btnCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSound) {
                    mp = MediaPlayer.create(GameActivity.this, R.raw.donkeykongcoin);
                    mp.start();
                }

                if (checkVibrate) {
                    vibrationChoice();
                }

                addPoint();

                tvCurrentWord.setText(getRandomWord());
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnContinue.getText().toString().equals("Ver resultados")) {
                    goToFinalScore();
                } else {
                    updateCountDownText();
                    resetTimer();
                    startTimer();

                    btnContinue.setVisibility(View.GONE);
                    tvCurrentWord.setVisibility(View.VISIBLE);
                    btnCorrect.setVisibility(View.VISIBLE);
                    btnPass.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        tvGTime.setText(timeLeftFormatted);
    }

    private void updateTeam() {
        currentTeam++;

        if (currentTeam < listTeams.size()) {
            String ct = listTeams.get(currentTeam);
            tvCurrentTeam.setText(ct);

            updateListCurrentWords();

            tvCurrentWord.setText(getRandomWord());

            String stateTeam = "Siguiente equipo";
            btnContinue.setText(stateTeam);
            tvCurrentWord.setVisibility(View.INVISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
            btnCorrect.setVisibility(View.GONE);
            btnPass.setVisibility(View.GONE);
        } else {
            updateRound();
        }
    }

    private void updateRound() {
        currentRound++;

        if (currentRound <= maxRounds) {
            currentTeam = 0;

            String ct = listTeams.get(currentTeam);
            tvCurrentTeam.setText(ct);

            updateListCurrentWords();

            tvCurrentWord.setText(getRandomWord());

            String strRound = String.valueOf(currentRound);
            tvCurrentRound.setText(strRound);

            String stateRound = "Siguiente ronda";
            btnContinue.setText(stateRound);
            tvCurrentWord.setVisibility(View.INVISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
            btnCorrect.setVisibility(View.GONE);
            btnPass.setVisibility(View.GONE);
        } else {
            if (checkSound) {
                mp = MediaPlayer.create(GameActivity.this, R.raw.finish);
                mp.start();
            }

            String stateResults = "Ver resultados";
            btnContinue.setText(stateResults);
            btnContinue.setVisibility(View.VISIBLE);
            btnCorrect.setVisibility(View.GONE);
            btnPass.setVisibility(View.GONE);
        }
    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("time", String.valueOf(millisUntilFinished));

                if (millisUntilFinished > 1000 && millisUntilFinished <= 5000) {
                    if (millisUntilFinished >= 4000) {
                        if (checkSound) {
                            mp = MediaPlayer.create(GameActivity.this, R.raw.tictac);
                            mp.start();
                        }
                    }

                    if (checkVibrate) {
                        vibrationTime();
                    }
                }

                timeLeft = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                updateTeam();
            }
        };

        countDownTimer.start();
    }

    private void resetTimer() {
        timeLeft = timeRound;
        updateCountDownText();
    }

    private void updateListCurrentWords() {
        listCurrentWords = new ArrayList<>();
        listCurrentWords.addAll(listWords);
    }

    private List<String> getTeams(String t) {
        List<String> list = new ArrayList<>();
        String current;
        int teams = Integer.valueOf(t);

        for (int i = 1; i <= teams; i++) {
            current = "nameTeam" + i;
            list.add(spSettingsGame.getString(current, null));
            listPoints.add(0);
        }

        return list;
    }

    private List<String> getWords(String w) {
        String[] strWords = w.split(Pattern.quote("|"));

        return new ArrayList<>(Arrays.asList(strWords));
    }

    private int getRounds(String r) {
        int round = 0;

        switch (r) {
            case "1 ronda":
                round = 1;
                break;
            case "3 rondas":
                round = 3;
                break;
            case "5 rondas":
                round = 5;
                break;
        }

        return round;
    }

    private int getTimeRound(String t) {
        int time = 0;

        switch (t) {
            case "15 segundos":
                time = 16000;
                break;
            case "30 segundos":
                time = 31000;
                break;
            case "45 segundos":
                time = 46000;
                break;
            case "1 minuto":
                time = 61000;
                break;
            case "3 minutos":
                time = 181000;
                break;
            case "5 minutos":
                time = 301000;
                break;
        }

        return  time;
    }

    private String getRandomWord() {
        String current;

        if (listCurrentWords.size() == 0) {
            Toast.makeText(this, "Ups, me dejaste sin palabras xD", Toast.LENGTH_SHORT).show();
            updateListCurrentWords();
        }

        Random rnd = new Random();
        int pos = rnd.nextInt(listCurrentWords.size());

        current = listCurrentWords.get(pos);
        listCurrentWords.remove(pos);

        return current;
    }

    private void addPoint() {
        int currentScore = listPoints.get(currentTeam);
        int newScore = currentScore + 1;
        listPoints.set(currentTeam, newScore);
    }

    private void convertPoints() {
        listPointsString = new ArrayList<>();

        for (int i = 0; i < listPoints.size(); i++) {
            listPointsString.add(String.valueOf(listPoints.get(i)));
        }
    }

    private void goToFinalScore() {
        convertPoints();

        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putStringArrayListExtra("listTeams", (ArrayList<String>) listTeams);
        intent.putStringArrayListExtra("listPoints", (ArrayList<String>) listPointsString);
        startActivity(intent, options.toBundle());
        finish();
    }

    private void vibrationChoice() {
        vibrator.vibrate(100);
    }

    private void vibrationTime() {
        long[] pattern = {0, 100, 150, 100};
        vibrator.vibrate(pattern, -1);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas salir del juego actual?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editorSettingsGame = spSettingsGame.edit();
                editorSettingsGame.clear();
                editorSettingsGame.apply();

                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent, options.toBundle());
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
