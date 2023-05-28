package com.merw_okuw_merkezi.bellikler.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BaseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}