package io.github.dalewahl.carddecks.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeckDao {
    //@Query("INSERT INTO deck (name, language, last) VALUES (:name, :language, :last)")
    //void create_deck(String name, String language, boolean last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDeck(Deck deck);

//    @Query("INSERT INTO category (deck_id, category) VALUES (:deck_id, :category)")
//    void create_category(int deck_id, String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

//    @Query("INSERT INTO card (deck_id, front_text, front_image, back_text, back_image) VALUES (:deck_id, :front_text, :front_image, :back_text, :back_image)")
//    void create_category(int deck_id, String front_text, String front_image, String back_text, String back_image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCard(Card card);

    @Query("SELECT * FROM deck WHERE last = 1")
    List<Deck> lastDeck();

    @Query("SELECT * FROM card WHERE deck_id = :deck_id")
    List<Card> getDeck(long deck_id);

    @Query("SELECT * FROM deck")
    List<Deck> allDecks();

    @Query("SELECT universal_id FROM deck")
    List<String> deckNames();

    @Query("UPDATE deck SET last = 0")
    void removeLast();

    @Query("UPDATE deck SET last = 1 WHERE id = :deck_id")
    void setLast(long deck_id);

    @Query("UPDATE deck SET deck_image = :deck_image WHERE id = :deck_id")
    void updateDeckImage(String deck_image, long deck_id);
}
