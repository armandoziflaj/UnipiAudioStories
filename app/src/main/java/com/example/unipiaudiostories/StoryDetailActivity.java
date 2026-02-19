package com.example.unipiaudiostories;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Locale;

/**
 * StoryDetailActivity displays the full details of a selected story.
 * It shows the story's image, title, author, and content, and provides
 * text-to-speech functionality to listen to the story. Play counts are
 * tracked both locally and in Firebase.
 */
public class StoryDetailActivity extends BaseActivity {

    /** Text-to-Speech engine for reading the story aloud */
    private TextToSpeech tts;
    /** Helper class for Firebase database operations */
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    /**
     * Called when the activity is starting. Sets up the UI with story details
     * and initializes the Text-to-Speech engine.
      */
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

    /**
     * Called before the activity is destroyed. Properly shuts down
     * the Text-to-Speech engine to release resources.
     */
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}