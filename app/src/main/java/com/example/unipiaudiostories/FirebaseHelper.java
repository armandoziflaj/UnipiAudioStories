package com.example.unipiaudiostories;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseHelper {
    private final DatabaseReference databaseReference;

    public FirebaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("stories");
    }

    public void getAllStories(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    public void getStoriesByPopularity(ValueEventListener listener) {
        databaseReference.orderByChild("playCount").addValueEventListener(listener);
    }

    public void incrementPlayCount(String storyId) {
        if (storyId == null) return;

        databaseReference.child(storyId).child("playCount")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer currentCount = mutableData.getValue(Integer.class);
                        if (currentCount == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentCount + 1);
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(com.google.firebase.database.DatabaseError error,
                                           boolean committed,
                                           com.google.firebase.database.DataSnapshot snapshot) {
                    }
                });
    }

    public void addStory(Story story) {
        String id = databaseReference.push().getKey();
        story.setId(id);
        databaseReference.child(Objects.requireNonNull(id)).setValue(story);
    }

    public void seedDatabase() {
        List<Story> stories = new ArrayList<>();

        stories.add(new Story("1", "The Lion and the Mouse", "Aesop",
                "A Lion was asleep when a little Mouse began running up and down upon him. The Lion placed his huge paw upon him and opened his big jaws to swallow him...",
                "https://i.pinimg.com/1200x/3d/6c/d3/3d6cd3aebbec9b5672fc556f8401e5e9.jpg", 0));

        stories.add(new Story("2", "The Boy Who Cried Wolf", "Aesop",
                "A shepherd boy, who watched a flock of sheep near a village, brought out the villagers three or four times by crying out, 'Wolf! Wolf!' and when his neighbors came to help him, he laughed at them for their pains...",
                "https://i.pinimg.com/1200x/0a/89/70/0a897023c81d8a094e2c2d0e8ab310e7.jpg", 0));

        stories.add(new Story("3", "The Tortoise and the Hare", "Aesop",
                "A Hare was making fun of the Tortoise one day for being so slow. 'Do you ever get anywhere?' he asked with a mocking laugh. 'Yes,' replied the Tortoise, 'and I get there sooner than you think'...",
                "https://i.pinimg.com/736x/1b/45/01/1b4501951f399ff810fdc0694a8ff594.jpg", 0));

        stories.add(new Story("4", "The Fox and the Grapes", "Aesop",
                "One hot summer's day a Fox was strolling through an orchard till he came to a bunch of Grapes just ripening on a vine which had been trained over a lofty branch. 'Just the thing to quench my thirst,' quoth he...",
                "https://i.pinimg.com/1200x/84/53/73/8453736c73898cf5baaa6441a85f42f1.jpg", 0));

        stories.add(new Story("5", "The Ants and the Grasshopper", "Aesop",
                "In a field one summer's day a Grasshopper was hopping about, chirping and singing to its heart's content. An Ant passed by, bearing along with great toil an ear of corn he was taking to the nest...",
                "https://i.pinimg.com/736x/d8/35/58/d83558a3de0798af75c711ecab782bb4.jpg", 0));

        for (Story s : stories) {
            databaseReference.child(s.getId()).setValue(s);
        }
    }
}
