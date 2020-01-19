package com.drm.mimicapp_v20;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navView;
    View viewNav;

    ImageView ivPhoto;
    TextView tvName, tvEmail;

    GoogleApiClient googleApiClient;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        navView = findViewById(R.id.navView);
        viewNav = navView.getHeaderView(0);

        drawerLayout = findViewById(R.id.drawer_main);
        ivPhoto = viewNav.findViewById(R.id.ivPhoto);
        tvName = viewNav.findViewById(R.id.tvName);
        tvEmail = viewNav.findViewById(R.id.tvEmail);

        options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this,
                                "Error en conexión con Google", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        TextView tvUserName = findViewById(R.id.tvUserName);
        Button btnNewGame = findViewById(R.id.btnNewGame);
        Button btnCategories = findViewById(R.id.btnCategories);

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        String userId = sharedPreferences.getString("userIdKey", null);

        if (userId == null) {
            userId = getIntent().getExtras().getString("userId");

            spEditor  = sharedPreferences.edit();
            spEditor.putString("userIdKey", userId);
            spEditor.apply();
        }

        String userName = sharedPreferences.getString("nameKey", null);

        if (userName != null) {
            String[] split = userName.split(" ");
            userName = split[0];

            userName = "Hola " + userName;
            tvUserName.setText(userName);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent, options.toBundle());
        }

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewGame();
            }
        });
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCategories();
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_settings:
                        goToSettings();
                        break;

                    case R.id.menu_signout:
                        signOut();
                        break;

                    case R.id.menu_exit:
                        exitApp();
                        break;

                    case R.id.menu_about:
                        goToAbout();
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.
                silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSingInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSingInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSingInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            if(account != null) {
                tvName.setText(account.getDisplayName());
                tvEmail.setText(account.getEmail());
                Glide.with(this).load(account.getPhotoUrl()).into(ivPhoto);
            }
        } else {
            Toast.makeText(this, "Error con tu sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveChoice(boolean b) {
        if (b) {
            Toast.makeText(this, "Active", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Inactive", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToNewGame() {
        loading();
        Intent iNewGame = new Intent(this, NewGameActivity.class);
        startActivity(iNewGame, options.toBundle());
    }

    private void goToCategories(){
        loading();
        Intent iCategories = new Intent( this, CategoryActivity.class);
        startActivity(iCategories, options.toBundle());
    }

    private void goToSettings() {
        Intent iSettings = new Intent(this, SettingsActivity.class);
        startActivity(iSettings, options.toBundle());
    }

    private void goToAbout(){
        Intent iAbout = new Intent( this, AboutActivity.class);
        startActivity(iAbout, options.toBundle());
    }

    private void signOut() {
        AlertDialog.Builder builderSignOut = new AlertDialog.Builder(this);
        builderSignOut.setMessage("¿Deseas cerrar sesión?");
        builderSignOut.setCancelable(true);

        builderSignOut.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builderSignOut.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            spEditor = sharedPreferences.edit();
                            spEditor.clear();
                            spEditor.apply();

                            Toast.makeText(MainActivity.this,
                                    "¡Hasta pronto!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "No se pudo cerrar sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        AlertDialog alertDialog = builderSignOut.create();
        alertDialog.show();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loading() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
    }

    private void exitApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas salir de la aplicación?");
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
                finishAffinity();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}