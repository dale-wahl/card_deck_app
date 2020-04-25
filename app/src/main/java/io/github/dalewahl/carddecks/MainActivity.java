package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

        // For testing/reloading decks
        // Who am I kidding? It's for when I load things COMPLETELY wrong.
        database.clearAllTables();

        InputStream inputStream = getResources().openRawResource(R.raw.playing_cards);
        new csvBuildDeck(inputStream, false);

        inputStream = getResources().openRawResource(R.raw.convo_starter);
        new csvBuildDeck(inputStream, true);

        //new DownloadDeck("https://raw.githubusercontent.com/dale-wahl/additional_decks/master/french_common_phrases.txt", getApplicationContext());

        lastButtonImage = findViewById(R.id.last_deck);
        List<Deck> decks = MainActivity.database.deckDao().lastDeck();
        Log.d("Main: Last Deck", "Number of last decks:" + decks.size());
        last_deck = decks.get(0);
        loadDeckImageButton(getApplicationContext(), lastButtonImage, last_deck);
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh image to last deck played
        List<Deck> decks = MainActivity.database.deckDao().lastDeck();
        last_deck = decks.get(0);
        loadDeckImageButton(getApplicationContext(), lastButtonImage, last_deck);
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

    public static Bitmap loadImageFromStorage(String path, String filename) {
        Bitmap b = null;
        try {
            File f=new File(path, filename);
            Log.d("loadImageFromStorage", "file f:" + f);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    public static void loadDeckImageButton(Context context, ImageButton button, Deck deck) {
        if (deck.resource_image) {
            button.setImageResource(context.getResources().getIdentifier(deck.deck_image, "drawable", context.getPackageName()));
        } else {
            button.setImageBitmap(loadImageFromStorage(deck.deck_image, deck.universal_id + "_image.png"));
        }
    }
}
