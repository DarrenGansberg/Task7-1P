package com.darrengansberg.noteapp.models;

import androidx.annotation.NonNull;

/* Represents/Models a note */
public class Note {

    private String content;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Note() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }
}
