package com.reobotetechnology.reobotegame.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;

public class BlogPostModel {

    private String id;
    private String title;
    private String description;
    private String reference;
    private String image;
    private String date;
    private String time;
    private String category;
    private FollowBlogPostModel follow;
    private CommentBlogPostModel comment;
    private int visualizations;

    public BlogPostModel(String id, String title, String description, String reference, String image, String date, String time, String category, FollowBlogPostModel follow, CommentBlogPostModel comment, int visualizations) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.image = image;
        this.date = date;
        this.time = time;
        this.category = category;
        this.follow = follow;
        this.comment = comment;
        this.visualizations = visualizations;
    }

    public BlogPostModel() {
    }

    @Exclude
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public FollowBlogPostModel getFollow() {
        return follow;
    }

    public void setFollow(FollowBlogPostModel follow) {
        this.follow = follow;
    }

    public CommentBlogPostModel getComment() {
        return comment;
    }

    public void setComment(CommentBlogPostModel comment) {
        this.comment = comment;
    }

    public int getVisualizations() {
        return visualizations;
    }

    public void setVisualizations(int visualizations) {
        this.visualizations = visualizations;
    }

    public void save(){
        DatabaseReference firebase = ConfigurationFireBase.getFirebaseDataBase();
        firebase.child("blog")
                .child((this.id))
                .setValue( this );
    }
}
