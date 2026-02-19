package com.example.unipiaudiostories;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * StoryAdapter is a RecyclerView adapter for displaying a list of stories.
 * It supports two modes: normal mode for browsing stories and statistics mode
 * for showing play counts alongside each story.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    /** List of stories to be displayed */
    private final List<Story> storyList;
    /** Listener for handling story item clicks */
    private final OnStoryClickListener listener;
    /** Flag indicating whether to show play count statistics */
    private final boolean showStats;

    /**
     * Interface for handling story item click events.
     */
    public interface OnStoryClickListener {
        void onStoryClick(Story story);
    }

    /**
     * Constructor for StoryAdapter.
   */
    public StoryAdapter(List<Story> storyList, boolean showStats, OnStoryClickListener listener) {
        this.storyList = storyList;
        this.showStats = showStats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storyList.get(position);
        Context context = holder.itemView.getContext();
        holder.title.setText(story.getTitle());
        if (showStats) {
            SharedPreferences prefs = context.getSharedPreferences("PlayCounts", Context.MODE_PRIVATE);
            int localPlayCount = prefs.getInt(story.getId(), 0);
            int finalDisplayCount = Math.max(story.getPlayCount(), localPlayCount);

            if (finalDisplayCount > 0) {
                holder.author.setText(story.getAuthor() + " • Plays: " + finalDisplayCount);
            } else {
                holder.author.setText(story.getAuthor());
            }
        } else {
            holder.author.setText(story.getAuthor());
        }

        if (story.getImageUrl() != null && story.getImageUrl().startsWith("http")) {
            Glide.with(context)
                    .load(story.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.image);
        } else if (story.getImageUrl() != null) {
            int resourceId = context.getResources().getIdentifier(
                    story.getImageUrl(),
                    "drawable",
                    context.getPackageName()
            );

            Glide.with(context)
                    .load(resourceId)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.image);
        }

        holder.itemView.setOnClickListener(v -> listener.onStoryClick(story));
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    /**
     * ViewHolder class for caching view references of story items.
     * Improves RecyclerView performance by avoiding repeated findViewById calls.
     */
    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView image;
        TextView title, author;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.storyImage);
            title = itemView.findViewById(R.id.storyTitle);
            author = itemView.findViewById(R.id.storyAuthor);
        }
    }
}
