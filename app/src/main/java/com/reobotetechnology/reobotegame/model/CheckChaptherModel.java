package com.reobotetechnology.reobotegame.model;

public class CheckChaptherModel {

    private int book_id;
    private int chapter_id;

    public CheckChaptherModel(int book_id, int chapter_id) {
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
