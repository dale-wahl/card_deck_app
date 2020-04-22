package io.github.dalewahl.carddecks.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Deck {
    @PrimaryKey (autoGenerate = true)
    public long id;

    @ColumnInfo(name = "universal_id")
    public String universal_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "language")
    public String language;

    @ColumnInfo(name = "deck_image")
    public String deck_image;

    @ColumnInfo(name = "last")
    public boolean last;
}
