package com.example.unipiaudiostories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private List<Story> storyList;
    private FirebaseHelper firebaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseHelper = new FirebaseHelper();
        storyList = new ArrayList<>();

        recyclerView = findViewById(R.id.storiesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StoryAdapter(storyList,false, story -> {
            Intent intent = new Intent(MainActivity.this, StoryDetailActivity.class);
            intent.putExtra("STORY_ID", story.getId());
            intent.putExtra("TITLE", story.getTitle());
            intent.putExtra("AUTHOR", story.getAuthor());
            intent.putExtra("CONTENT", story.getContent());
            intent.putExtra("IMAGE_URL", story.getImageUrl());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        Button btnStats = findViewById(R.id.btnStats);
        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Statistics.class);
            startActivity(intent);
        });
        //firebaseHelper.seedDatabase();

        loadStories();
        findViewById(R.id.btnSettings).setOnClickListener(v -> showSettingsDialog());
    }

    private void loadStories() {
        firebaseHelper.getAllStories(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storyList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Story story = data.getValue(Story.class);
                    if (story != null) {
                        storyList.add(story);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void showSettingsDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_settings, null);
        bottomSheetDialog.setContentView(view);

        MaterialSwitch switchDark = view.findViewById(R.id.switchDarkMode);
        RadioGroup rgLang = view.findViewById(R.id.rgLanguage);

        switchDark.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
            editor.putBoolean("My_Night_Mode", isChecked);
            editor.apply();
        });

        rgLang.setOnCheckedChangeListener((group, checkedId) -> {
            String langCode = "en";
            if (checkedId == R.id.rbEl) langCode = "el";
            else if (checkedId == R.id.rbFr) langCode = "fr";

            getSharedPreferences("Settings", MODE_PRIVATE).edit()
                    .putString("My_Lang", langCode).apply();

            bottomSheetDialog.dismiss();

            recreate();
        });

        bottomSheetDialog.show();
    }
}