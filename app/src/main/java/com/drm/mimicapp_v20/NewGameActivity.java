package com.drm.mimicapp_v20;

import android.animation.LayoutTransition;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.drm.mimicapp_v20.db_connection.ListCategories;

public class NewGameActivity extends AppCompatActivity {
    SharedPreferences spNewGame;
    SharedPreferences.Editor editorNewGame;

    ActivityOptions options;

    ProgressDialog progressDialog = null;

    EditText etNameTeamOne, etNameTeamTwo, etNameTeamThree, etNameTeamFour, etNameTeamFive,
            etNameTeamSix, etNameTeamSeven;

    Spinner sNumberTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        setToolbar();

        progressDialog = new ProgressDialog(this);

        LinearLayout linearLayout = findViewById(R.id.linSpin);
        linearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        spNewGame = getSharedPreferences("settingsNewGame", Context.MODE_PRIVATE);

        options = ActivityOptions.makeSceneTransitionAnimation(NewGameActivity.this);

        etNameTeamOne = findViewById(R.id.etNameTeam1);
        etNameTeamTwo = findViewById(R.id.etNameTeam2);
        etNameTeamThree = findViewById(R.id.etNameTeam3);
        etNameTeamFour = findViewById(R.id.etNameTeam4);
        etNameTeamFive = findViewById(R.id.etNameTeam5);
        etNameTeamSix = findViewById(R.id.etNameTeam6);
        etNameTeamSeven = findViewById(R.id.etNameTeam7);

        etNameTeamOne.setVisibility(View.GONE);
        etNameTeamTwo.setVisibility(View.GONE);
        etNameTeamThree.setVisibility(View.GONE);
        etNameTeamFour.setVisibility(View.GONE);
        etNameTeamFive.setVisibility(View.GONE);
        etNameTeamSix.setVisibility(View.GONE);
        etNameTeamSeven.setVisibility(View.GONE);

        final Button btnNext = findViewById(R.id.btnNGNext);

        sNumberTeams = findViewById(R.id.sNumberTeams);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.number_teams, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sNumberTeams.setAdapter(adapter);

        if (spNewGame != null) {
            int teams = Integer.parseInt(spNewGame.getString("numberTeamsKey", "0"));
            teams = teams - 2;

            if (teams >= 0) {
                sNumberTeams.setSelection(teams);

                switch (teams) {
                    case 0:
                        etNameTeamOne.setText(spNewGame.getString("nameTeam1", null));
                        etNameTeamTwo.setText(spNewGame.getString("nameTeam2", null));

                        break;
                    case 1:
                        etNameTeamOne.setText(spNewGame.getString("nameTeam1", null));
                        etNameTeamTwo.setText(spNewGame.getString("nameTeam2", null));
                        etNameTeamThree.setText(spNewGame.getString("nameTeam3", null));

                        break;
                    case 2:
                        etNameTeamOne.setText(spNewGame.getString("nameTeam1", null));
                        etNameTeamTwo.setText(spNewGame.getString("nameTeam2", null));
                        etNameTeamThree.setText(spNewGame.getString("nameTeam3", null));
                        etNameTeamFour.setText(spNewGame.getString("nameTeam4", null));

                        break;
                    case 3:
                        etNameTeamOne.setText(spNewGame.getString("nameTeam1", null));
                        etNameTeamTwo.setText(spNewGame.getString("nameTeam2", null));
                        etNameTeamThree.setText(spNewGame.getString("nameTeam3", null));
                        etNameTeamFour.setText(spNewGame.getString("nameTeam4", null));
                        etNameTeamFive.setText(spNewGame.getString("nameTeam5", null));

                        break;
                    case 4:
                        etNameTeamOne.setText(spNewGame.getString("nameTeam1", null));
                        etNameTeamTwo.setText(spNewGame.getString("nameTeam2", null));
                        etNameTeamThree.setText(spNewGame.getString("nameTeam3", null));
                        etNameTeamFour.setText(spNewGame.getString("nameTeam4", null));
                        etNameTeamFive.setText(spNewGame.getString("nameTeam5", null));
                        etNameTeamSix.setText(spNewGame.getString("nameTeam6", null));

                        break;
                    case 5:
                        etNameTeamOne.setText(spNewGame.getString("nameTeam1", null));
                        etNameTeamTwo.setText(spNewGame.getString("nameTeam2", null));
                        etNameTeamThree.setText(spNewGame.getString("nameTeam3", null));
                        etNameTeamFour.setText(spNewGame.getString("nameTeam4", null));
                        etNameTeamFive.setText(spNewGame.getString("nameTeam5", null));
                        etNameTeamSix.setText(spNewGame.getString("nameTeam6", null));
                        etNameTeamSeven.setText(spNewGame.getString("nameTeam7", null));

                        break;
                    default:
                        break;
                }
            }
        }

        sNumberTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        etNameTeamOne.setVisibility(View.VISIBLE);
                        etNameTeamTwo.setVisibility(View.VISIBLE);
                        etNameTeamThree.setVisibility(View.GONE);
                        etNameTeamFour.setVisibility(View.GONE);
                        etNameTeamFive.setVisibility(View.GONE);
                        etNameTeamSix.setVisibility(View.GONE);
                        etNameTeamSeven.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);

                        editorNewGame = spNewGame.edit();
                        editorNewGame.putString("numberTeamsKey", "2");
                        editorNewGame.apply();


                        break;
                    case 1:
                        etNameTeamOne.setVisibility(View.VISIBLE);
                        etNameTeamTwo.setVisibility(View.VISIBLE);
                        etNameTeamThree.setVisibility(View.VISIBLE);
                        etNameTeamFour.setVisibility(View.GONE);
                        etNameTeamFive.setVisibility(View.GONE);
                        etNameTeamSix.setVisibility(View.GONE);
                        etNameTeamSeven.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);

                        editorNewGame = spNewGame.edit();
                        editorNewGame.putString("numberTeamsKey", "3");
                        editorNewGame.apply();

                        break;
                    case 2:
                        etNameTeamOne.setVisibility(View.VISIBLE);
                        etNameTeamTwo.setVisibility(View.VISIBLE);
                        etNameTeamThree.setVisibility(View.VISIBLE);
                        etNameTeamFour.setVisibility(View.VISIBLE);
                        etNameTeamFive.setVisibility(View.GONE);
                        etNameTeamSix.setVisibility(View.GONE);
                        etNameTeamSeven.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);

                        editorNewGame = spNewGame.edit();
                        editorNewGame.putString("numberTeamsKey", "4");
                        editorNewGame.apply();

                        break;
                    case 3:
                        etNameTeamOne.setVisibility(View.VISIBLE);
                        etNameTeamTwo.setVisibility(View.VISIBLE);
                        etNameTeamThree.setVisibility(View.VISIBLE);
                        etNameTeamFour.setVisibility(View.VISIBLE);
                        etNameTeamFive.setVisibility(View.VISIBLE);
                        etNameTeamSix.setVisibility(View.GONE);
                        etNameTeamSeven.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);

                        editorNewGame = spNewGame.edit();
                        editorNewGame.putString("numberTeamsKey", "5");
                        editorNewGame.apply();

                        break;
                    case 4:
                        etNameTeamOne.setVisibility(View.VISIBLE);
                        etNameTeamTwo.setVisibility(View.VISIBLE);
                        etNameTeamThree.setVisibility(View.VISIBLE);
                        etNameTeamFour.setVisibility(View.VISIBLE);
                        etNameTeamFive.setVisibility(View.VISIBLE);
                        etNameTeamSix.setVisibility(View.VISIBLE);
                        etNameTeamSeven.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);

                        editorNewGame = spNewGame.edit();
                        editorNewGame.putString("numberTeamsKey", "6");
                        editorNewGame.apply();

                        break;
                    case 5:
                        etNameTeamOne.setVisibility(View.VISIBLE);
                        etNameTeamTwo.setVisibility(View.VISIBLE);
                        etNameTeamThree.setVisibility(View.VISIBLE);
                        etNameTeamFour.setVisibility(View.VISIBLE);
                        etNameTeamFive.setVisibility(View.VISIBLE);
                        etNameTeamSix.setVisibility(View.VISIBLE);
                        etNameTeamSeven.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);

                        editorNewGame = spNewGame.edit();
                        editorNewGame.putString("numberTeamsKey", "7");
                        editorNewGame.apply();

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userName = Integer.parseInt(spNewGame.getString("numberTeamsKey", null));

                switch (userName) {
                    case 2:
                        if (!etNameTeamOne.getText().toString().isEmpty() &&
                                !etNameTeamTwo.getText().toString().isEmpty()) {
                            editorNewGame = spNewGame.edit();
                            editorNewGame.putString("nameTeam1", String.valueOf(
                                    etNameTeamOne.getText()));
                            editorNewGame.putString("nameTeam2", String.valueOf(
                                    etNameTeamTwo.getText()));
                            editorNewGame.apply();

                            changeActivity();
                        } else {
                            verifyName(etNameTeamOne);
                            verifyName(etNameTeamTwo);
                        }

                        break;

                    case 3:
                        if (!etNameTeamOne.getText().toString().isEmpty() &&
                                !etNameTeamTwo.getText().toString().isEmpty() &&
                                !etNameTeamThree.getText().toString().isEmpty()) {
                            editorNewGame = spNewGame.edit();
                            editorNewGame.putString("nameTeam1", String.valueOf(
                                    etNameTeamOne.getText()));
                            editorNewGame.putString("nameTeam2", String.valueOf(
                                    etNameTeamTwo.getText()));
                            editorNewGame.putString("nameTeam3", String.valueOf(
                                    etNameTeamThree.getText()));
                            editorNewGame.apply();

                            changeActivity();
                        } else {
                            verifyName(etNameTeamOne);
                            verifyName(etNameTeamTwo);
                            verifyName(etNameTeamThree);
                        }

                        break;

                    case 4:
                        if (!etNameTeamOne.getText().toString().isEmpty()&&
                                !etNameTeamTwo.getText().toString().isEmpty() &&
                                !etNameTeamThree.getText().toString().isEmpty() &&
                                !etNameTeamFour.getText().toString().isEmpty()) {
                            editorNewGame = spNewGame.edit();
                            editorNewGame.putString("nameTeam1", String.valueOf(
                                    etNameTeamOne.getText()));
                            editorNewGame.putString("nameTeam2", String.valueOf(
                                    etNameTeamTwo.getText()));
                            editorNewGame.putString("nameTeam3", String.valueOf(
                                    etNameTeamThree.getText()));
                            editorNewGame.putString("nameTeam4", String.valueOf(
                                    etNameTeamFour.getText()));
                            editorNewGame.apply();

                            changeActivity();
                        } else {
                            verifyName(etNameTeamOne);
                            verifyName(etNameTeamTwo);
                            verifyName(etNameTeamThree);
                            verifyName(etNameTeamFour);
                        }

                        break;

                    case 5:
                        if (!etNameTeamOne.getText().toString().isEmpty() &&
                                !etNameTeamTwo.getText().toString().isEmpty() &&
                                !etNameTeamThree.getText().toString().isEmpty() &&
                                !etNameTeamFour.getText().toString().isEmpty() &&
                                !etNameTeamFive.getText().toString().isEmpty()) {
                            editorNewGame = spNewGame.edit();
                            editorNewGame.putString("nameTeam1", String.valueOf(
                                    etNameTeamOne.getText()));
                            editorNewGame.putString("nameTeam2", String.valueOf(
                                    etNameTeamTwo.getText()));
                            editorNewGame.putString("nameTeam3", String.valueOf(
                                    etNameTeamThree.getText()));
                            editorNewGame.putString("nameTeam4", String.valueOf(
                                    etNameTeamFour.getText()));
                            editorNewGame.putString("nameTeam5", String.valueOf(
                                    etNameTeamFive.getText()));
                            editorNewGame.apply();

                            changeActivity();
                        } else {
                            verifyName(etNameTeamOne);
                            verifyName(etNameTeamTwo);
                            verifyName(etNameTeamThree);
                            verifyName(etNameTeamFour);
                            verifyName(etNameTeamFive);
                        }

                        break;

                    case 6:
                        if (!etNameTeamOne.getText().toString().isEmpty() &&
                                !etNameTeamTwo.getText().toString().isEmpty() &&
                                !etNameTeamThree.getText().toString().isEmpty() &&
                                !etNameTeamFour.getText().toString().isEmpty() &&
                                !etNameTeamFive.getText().toString().isEmpty() &&
                                !etNameTeamSix.getText().toString().isEmpty()) {
                            editorNewGame = spNewGame.edit();
                            editorNewGame.putString("nameTeam1", String.valueOf(
                                    etNameTeamOne.getText()));
                            editorNewGame.putString("nameTeam2", String.valueOf(
                                    etNameTeamTwo.getText()));
                            editorNewGame.putString("nameTeam3nameTeam3", String.valueOf(
                                    etNameTeamThree.getText()));
                            editorNewGame.putString("nameTeam4nameTeam4", String.valueOf(
                                    etNameTeamFour.getText()));
                            editorNewGame.putString("nameTeam5", String.valueOf(
                                    etNameTeamFive.getText()));
                            editorNewGame.putString("nameTeam6", String.valueOf(
                                    etNameTeamSix.getText()));
                            editorNewGame.apply();

                            changeActivity();
                        } else {
                            verifyName(etNameTeamOne);
                            verifyName(etNameTeamTwo);
                            verifyName(etNameTeamThree);
                            verifyName(etNameTeamFour);
                            verifyName(etNameTeamFive);
                            verifyName(etNameTeamSix);
                        }

                        break;

                    case 7:
                        if (!etNameTeamOne.getText().toString().isEmpty() &&
                                !etNameTeamTwo.getText().toString().isEmpty() &&
                                !etNameTeamThree.getText().toString().isEmpty() &&
                                !etNameTeamFour.getText().toString().isEmpty() &&
                                !etNameTeamFive.getText().toString().isEmpty() &&
                                !etNameTeamSix.getText().toString().isEmpty() &&
                                !etNameTeamSeven.getText().toString().isEmpty()) {
                            editorNewGame = spNewGame.edit();
                            editorNewGame.putString("nameTeam1", String.valueOf(
                                    etNameTeamOne.getText()));
                            editorNewGame.putString("nameTeam2", String.valueOf(
                                    etNameTeamTwo.getText()));
                            editorNewGame.putString("nameTeam3", String.valueOf(
                                    etNameTeamThree.getText()));
                            editorNewGame.putString("nameTeam4", String.valueOf(
                                    etNameTeamFour.getText()));
                            editorNewGame.putString("nameTeam5", String.valueOf(
                                    etNameTeamFive.getText()));
                            editorNewGame.putString("nameTeam6", String.valueOf(
                                    etNameTeamSix.getText()));
                            editorNewGame.putString("nameTeam7", String.valueOf(
                                    etNameTeamSeven.getText()));
                            editorNewGame.apply();

                            changeActivity();
                        } else {
                            verifyName(etNameTeamOne);
                            verifyName(etNameTeamTwo);
                            verifyName(etNameTeamThree);
                            verifyName(etNameTeamFour);
                            verifyName(etNameTeamFive);
                            verifyName(etNameTeamSix);
                            verifyName(etNameTeamSeven);
                        }

                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        sNumberTeams.setSelection(0);

        etNameTeamOne.setText("");
        etNameTeamTwo.setText("");
        etNameTeamThree.setText("");
        etNameTeamFour.setText("");
        etNameTeamFive.setText("");
        etNameTeamSix.setText("");
        etNameTeamSeven.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();

        progressDialog.dismiss();
    }

    private void verifyName(EditText currentEditText) {
        if (currentEditText.getText().toString().isEmpty()) {
            currentEditText.setError("Campo obligatorio");
        }
    }

    private void changeActivity() {
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        SharedPreferences spUser = getSharedPreferences("userData", Context.MODE_PRIVATE);
        String url = spUser.getString("urlKey", null);
        String userId = spUser.getString("userIdKey", null);

        new ListCategories(this).execute(url, userId);
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
        builder.setMessage("¿Deseas volver al menú principal?");
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
                editorNewGame = spNewGame.edit();
                editorNewGame.clear();
                editorNewGame.apply();

                Intent intent = new Intent(NewGameActivity.this, MainActivity.class);
                startActivity(intent, options.toBundle());
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
