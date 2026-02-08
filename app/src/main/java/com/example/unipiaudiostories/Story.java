package com.example.unipiaudiostories;

public class Story {
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private String author;
    private int playCount;
    public Story() {
    }

    public Story(String id, String title, String author, String content, String imageUrl, int playCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.imageUrl = imageUrl;
        this.playCount = playCount;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }
}