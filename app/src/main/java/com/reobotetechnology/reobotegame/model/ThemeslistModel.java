package com.reobotetechnology.reobotegame.model;

public class ThemeslistModel {

    private int book_id;
    private int chapter_id;
    private int verse_id;

    private String themeName;

    public ThemeslistModel(int book_id, int chapter_id, int verse_id, String themeName) {
        this.book_id = book_id;
        this.chapter_id = chapter_id;
        this.verse_id = verse_id;
        this.themeName = themeName;
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

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
