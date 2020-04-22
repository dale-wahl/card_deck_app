package io.github.dalewahl.carddecks.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey (autoGenerate = true)
    public long id;

    @ColumnInfo(name = "deck_id")
    public long deck_id;

    @ColumnInfo(name = "category")
    public String category;
}
