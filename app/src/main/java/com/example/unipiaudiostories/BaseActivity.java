package com.example.unipiaudiostories;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);

        boolean isNightMode = prefs.getBoolean("My_Night_Mode", false);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "en");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        android.content.res.Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(locale);

        android.content.Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }
}
