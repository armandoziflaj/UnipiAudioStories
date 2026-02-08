package com.example.unipiaudiostories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.imageview.ShapeableImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private List<Story> storyList;
    private OnStoryClickListener listener;

    public interface OnStoryClickListener {
        void onStoryClick(Story story);
    }

    public StoryAdapter(List<Story> storyList, OnStoryClickListener listener) {
        this.storyList = storyList;
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
        holder.title.setText(story.getTitle());
        holder.author.setText(story.getAuthor());

        if (story.getPlayCount() > 0) {
            holder.author.setText("Plays: " + story.getPlayCount());
        } else {
            holder.author.setText(story.getAuthor());
        }
        Context context = holder.itemView.getContext();

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
