package com.example.unipiaudiostories;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Statistics activity displays stories sorted by popularity (play count).
 * It shows the most popular stories in descending order, allowing users
 * to see which stories are most listened to.
 */
public class Statistics extends BaseActivity {

    /** Adapter for displaying stories with statistics */
    private StoryAdapter adapter;
    /** List containing stories sorted by popularity */
    private List<Story> statsList;
    /** Helper class for Firebase database operations */
    private FirebaseHelper firebaseHelper;

    /**
     * Called when the activity is starting. Sets up the RecyclerView
     * and loads statistics data from Firebase.
     *
    
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_stats), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.rvStatistics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        statsList = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();
        adapter = new StoryAdapter(statsList, true, story -> {
        });
        recyclerView.setAdapter(adapter);

        loadStatistics();
    }

    /**
     * Loads stories ordered by popularity from Firebase.
     * Stories are retrieved in ascending order by playCount and then
     * reversed to display the most popular stories first.
     */
    private void loadStatistics() {
        firebaseHelper.getStoriesByPopularity(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Story story = postSnapshot.getValue(Story.class);
                    if (story != null) {
                        statsList.add(story);
                    }
                }
                Collections.reverse(statsList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}