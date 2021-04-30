package com.reobotetechnology.reobotegame.model;

public class CheckVerseModel {

    private int book_id;
    private int chapter_id;
    private int verse_id;
    private String color;

    public CheckVerseModel(int book_id, int chapter_id) {
        this.book_id = book_id;
        this.chapter_id = chapter_id;
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
}
