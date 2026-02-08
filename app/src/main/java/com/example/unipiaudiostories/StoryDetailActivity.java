package com.example.unipiaudiostories;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class StoryDetailActivity extends BaseActivity {

    private TextToSpeech tts;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        TextView tvDetailTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDetailContent = findViewById(R.id.tvDetailContent);
        TextView tvAuthorName = findViewById(R.id.tvAuthorName);
        Button btnListen = findViewById(R.id.btnListen);

        String storyId = getIntent().getStringExtra("STORY_ID");
        String title = getIntent().getStringExtra("TITLE");
        String author = getIntent().getStringExtra("AUTHOR");
        String content = getIntent().getStringExtra("CONTENT");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        tvDetailTitle.setText(title);
        tvDetailContent.setText(content);
        tvAuthorName.setText(author);
        Glide.with(this).load(imageUrl).into(ivDetailImage);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        btnListen.setOnClickListener(v -> {
            if (content != null) {
                tts.speak(content, TextToSpeech.QUEUE_FLUSH, null, null);
                firebaseHelper.incrementPlayCount(storyId);
                SharedPreferences cache = getSharedPreferences("PlayCounts", MODE_PRIVATE);
                int currentCount = cache.getInt(storyId, 0);
                cache.edit().putInt(storyId, currentCount + 1).apply();
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}