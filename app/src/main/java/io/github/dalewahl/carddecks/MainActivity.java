package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.InputStream;
import java.util.List;

import io.github.dalewahl.carddecks.database.Deck;
import io.github.dalewahl.carddecks.database.DecksDatabase;

public class MainActivity extends AppCompatActivity {
    public static DecksDatabase database;
    private ImageButton lastButtonImage;
    private Deck last_deck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room
                .databaseBuilder(getApplicationContext(), DecksDatabase.class, "decks")
                // TODO DON'T FORGET THAT THIS IS PROBABLY NOT HOW TO HANDLE DATABASE CHANGES!!!
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        InputStream inputStream = getResources().openRawResource(R.raw.playing_cards);
        new csvBuildDeck(inputStream, false);

        inputStream = getResources().openRawResource(R.raw.convo_starter);
        new csvBuildDeck(inputStream, true);


        lastButtonImage = findViewById(R.id.last_deck);
        List<Deck> decks = MainActivity.database.deckDao().lastDeck();
        Log.d("Main: Last Deck", "Number of last decks:" + decks.size());
        last_deck = decks.get(0);
        lastButtonImage.setImageResource(getResources().getIdentifier(last_deck.deck_image, "drawable", getPackageName()));
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh image to last deck played
        List<Deck> decks = MainActivity.database.deckDao().lastDeck();
        last_deck = decks.get(0);
        lastButtonImage.setImageResource(getResources().getIdentifier(last_deck.deck_image, "drawable", getPackageName()));
    }

    public void openLastDeck(View view) {
        Intent intent = new Intent(this, DeckActivity.class);
        intent.putExtra("deck_id", last_deck.id);
        Log.d("Main: Last Deck Button", "Open Last Deck ID:" + last_deck.id);
        startActivity(intent);
    }

    public void chooseDeck(View view) {
       Intent intent = new Intent(this, ChooseActivity.class);
        Log.d("Main: Choose Button", "Choosing Deck");
        startActivity(intent);
    }
}
