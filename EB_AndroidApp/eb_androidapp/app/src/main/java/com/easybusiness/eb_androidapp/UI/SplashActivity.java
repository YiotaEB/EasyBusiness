package com.easybusiness.eb_androidapp.UI;

import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.easybusiness.eb_androidapp.AsyncTask.LoginExistingAsyncTask;
import com.easybusiness.eb_androidapp.R;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_DISPLAY_LENGTH = 1000;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        view = findViewById(R.id.splash_activity);

        String username = PreferenceManager.getDefaultSharedPreferences(this).getString(MainActivity.PREFERENCE_USERNAME, null);
        String password = PreferenceManager.getDefaultSharedPreferences(this).getString(MainActivity.PREFERENCE_PASSWORD_HASH, null);

        if (username == null || password == null) {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        else {
            new LoginExistingAsyncTask(this, view, username, password).execute();
        }



    }
}