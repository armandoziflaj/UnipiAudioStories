package com.example.unipiaudiostories;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

/**
 * BaseActivity is the parent activity for all activities in the application.
 * It handles common functionality such as theme management (night mode)
 * and language localization across all child activities.
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. Applies the saved night mode preference
     * before calling the superclass onCreate method.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                          being shut down, this Bundle contains the data it most
     *                          recently supplied in onSaveInstanceState.
     */
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

    /**
     * Attaches the base context with the appropriate locale configuration.
     * This method reads the saved language preference and applies it to the
     * activity's context, enabling multi-language support.
     *
     * @param newBase The new base context for this activity.
     */
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
