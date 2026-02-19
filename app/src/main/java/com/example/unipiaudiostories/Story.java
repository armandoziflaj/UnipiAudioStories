package com.example.unipiaudiostories;

/**
 * Story is a model class representing an audio story.
 * It contains all the information about a story including title,
 * author, content, image URL, and play count statistics.
 */
public class Story {
    /** Unique identifier for the story */
    private String id;
    /** Title of the story */
    private String title;
    /** Full text content of the story */
    private String content;
    /** URL of the story's cover image */
    private String imageUrl;
    /** Author name of the story */
    private String author;
    /** Number of times the story has been played */
    private int playCount;

    /**
     * Default constructor required for Firebase deserialization.
     */
    public Story() {
    }

    /**
     * Parameterized constructor for creating a Story object.
     *
    
     */
    public Story(String id, String title, String author, String content, String imageUrl, int playCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.imageUrl = imageUrl;
        this.playCount = playCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}