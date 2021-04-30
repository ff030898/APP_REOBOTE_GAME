package com.reobotetechnology.reobotegame.model;

public class DescriptionBookModel {

    private int book_id;
    private String sigle;
    private String author;
    private String description;
    private int availabled;
    private int favorited;
    private int scoreLearning;
    private String date;
    private int learning;
    private String reference;

    public DescriptionBookModel(int book_id, String sigle, String author, String description, int availabled, int favorited, int scoreLearning, String date, int learning, String reference) {
        this.book_id = book_id;
        this.sigle = sigle;
        this.author = author;
        this.description = description;
        this.availabled = availabled;
        this.favorited = favorited;
        this.scoreLearning = scoreLearning;
        this.date = date;
        this.learning = learning;
        this.reference = reference;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailabled() {
        return availabled;
    }

    public void setAvailabled(int availabled) {
        this.availabled = availabled;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }

    public int getScoreLearning() {
        return scoreLearning;
    }

    public void setScoreLearning(int scoreLearning) {
        this.scoreLearning = scoreLearning;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLearning() {
        return learning;
    }

    public void setLearning(int learning) {
        this.learning = learning;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
