package io.github.dalewahl.carddecks.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Deck.class, Category.class, Card.class}, version = 3, exportSchema = false)
public abstract class DecksDatabase extends RoomDatabase {
    public abstract DeckDao deckDao();
}
