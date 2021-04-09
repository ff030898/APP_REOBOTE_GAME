package com.reobotetechnology.reobotegame.model;

public class VerseDayModel {

    private int id;
    private int book_id;
    private int chapter_id;
    private int verse_id;
    private String date;
    private String time;

    public VerseDayModel(int id, int book_id, int chapter_id, int verse_id, String date, String time) {
        this.id = id;
        this.book_id = book_id;
        this.chapter_id = chapter_id;
        this.verse_id = verse_id;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getVerse_id() {
        return verse_id;
    }

    public void setVerse_id(int verse_id) {
        this.verse_id = verse_id;
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
}
