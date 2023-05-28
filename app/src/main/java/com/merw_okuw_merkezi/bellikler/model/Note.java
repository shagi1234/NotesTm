package com.merw_okuw_merkezi.bellikler.model;

import androidx.room.Entity;

@Entity(tableName = "note")
public class Note extends BaseEntity {
    private String title;
    private String content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
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
}
