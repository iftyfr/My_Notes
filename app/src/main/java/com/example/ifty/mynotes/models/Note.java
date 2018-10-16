package com.example.ifty.mynotes.models;

public class Note {
    private int id;
    private String title;
    private String note;
    private String date;
    private String photoPath;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getDateNtime() {
        return date;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public Note(String title, String note, String date, String photoPath) {

        this.title = title;
        this.note = note;
        this.date = date;
        this.photoPath = photoPath;
    }

    public Note(int id, String title, String note, String date, String photoPath) {

        this.id = id;
        this.title = title;
        this.note = note;
        this.date = date;
        this.photoPath = photoPath;
    }
}
