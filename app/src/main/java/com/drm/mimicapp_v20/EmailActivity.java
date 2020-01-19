package com.drm.mimicapp_v20;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity {
    private EditText etSubject, etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        setToolbar();

        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        Button btnSend = findViewById(R.id.btnSendEmail);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        if (!etSubject.getText().toString().isEmpty() && !etMessage.getText().toString().isEmpty()) {
            sendEmail();
        } else {
            Toast.makeText(this, "Debes llenar los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        String subject, message;
        String[] to = {"mimicappdeveloper@gmail.com"};

        subject = etSubject.getText().toString();
        message = etMessage.getText().toString();

        Intent iEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        iEmail.putExtra(Intent.EXTRA_EMAIL, to);
        iEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        iEmail.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(iEmail, "Opciones"));
        finish();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }
}
