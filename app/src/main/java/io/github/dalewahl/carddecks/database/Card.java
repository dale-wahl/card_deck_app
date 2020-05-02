package io.github.dalewahl.carddecks.database;

import android.text.Editable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Card {
    @PrimaryKey (autoGenerate = true)
    public long id;

    @ColumnInfo(name = "deck_id")
    public long deck_id;

    @ColumnInfo(name = "front_text")
    public String front_text;

    @ColumnInfo(name = "front_image")
    public String front_image;

    @ColumnInfo(name = "back_text")
    public String back_text;

    @ColumnInfo(name = "back_image")
    public String back_image;
}
