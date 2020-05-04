package io.github.dalewahl.carddecks.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDeck(Deck deck);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCard(Card card);

    @Query("SELECT * FROM deck WHERE last = 1")
    List<Deck> lastDeck();

    @Query("SELECT * FROM card WHERE deck_id = :deck_id")
    List<Card> getDeck(long deck_id);

    @Query("SELECT * FROM deck")
    List<Deck> allDecks();

    @Query("SELECT universal_id FROM deck")
    List<String> deckNames();

    @Query("SELECT name FROM deck WHERE id = :deck_id")
    String deckName(long deck_id);

    @Query("SELECT category FROM category WHERE deck_id = :deck_id")
    List<String> deckCategories(long deck_id);

    @Query("UPDATE deck SET last = 0")
    void removeLast();

    @Query("UPDATE deck SET last = 1 WHERE id = :deck_id")
    void setLast(long deck_id);

    @Query("UPDATE card SET front_text = :front_text, back_text = :back_text WHERE id = :id")
    void saveCard(long id, String front_text, String back_text);

    @Query("UPDATE deck SET deck_image = :deck_image WHERE id = :deck_id")
    void updateDeckImage(String deck_image, long deck_id);

    @Query ("DELETE FROM deck WHERE id = :deck_id")
    void deleteDeck(long deck_id);

    @Query ("DELETE FROM card WHERE deck_id = :deck_id")
    void deleteDeckCards(long deck_id);

    @Query ("DELETE FROM category WHERE deck_id = :deck_id")
    void deleteDeckCategories(long deck_id);

    @Query("DELETE FROM card WHERE id = :id")
    void deleteCard(long id);
}
