package com.example.elli.uchews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class LoginActivity extends AppCompatActivity {

    Button reg_btn;
    ProgressBar progressBar;
    private static final String PREFS_UNIQUE_IDENTIFIER = "com.example.uchews.user.data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reg_btn = (Button) findViewById(R.id.register);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        //Checks to see if preferences exist
        SharedPreferences sharedPrefs = this.getSharedPreferences(PREFS_UNIQUE_IDENTIFIER, Context.MODE_PRIVATE);
        if(sharedPrefs.contains("EMAIL")) {
            reg_btn.setVisibility(View.GONE);
            //progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, 2000);*/
        }

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegister();
            }
        });
    }

    public void toRegister() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
