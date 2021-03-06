package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                // This might delete someone's decks on upgrade if we change the database....
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        // For testing/reloading decks
        // Who am I kidding? It's for when I load things COMPLETELY wrong.
        //database.clearAllTables();

        // Load the two base decks
        InputStream inputStream = getResources().openRawResource(R.raw.playing_cards);
        new csvBuildDeck(inputStream, true);

        inputStream = getResources().openRawResource(R.raw.convo_starter);
        new csvBuildDeck(inputStream, false);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lastButtonImage = findViewById(R.id.last_deck);
        List<Deck> decks = MainActivity.database.deckDao().lastDeck();
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
        startActivity(intent);
    }

    public void chooseDeck(View view) {
       Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
    }

    public static Bitmap loadImageFromStorage(String path, String filename) {
        Bitmap b = null;
        try {
            File f=new File(path, filename);
            FileInputStream input = new FileInputStream(f);
            b = BitmapFactory.decodeStream(input);
            input.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void loadDeckImageButton(Context context, ImageButton button, Deck deck) {
        if (deck.resource_image) {
            if (!deck.deck_image.equals("NULL")) {
                button.setImageResource(context.getResources().getIdentifier(deck.deck_image, "drawable", context.getPackageName()));
            } else {
                button.setImageResource(context.getResources().getIdentifier("blank_back", "drawable", context.getPackageName()));
            }
        }else {
            button.setImageBitmap(loadImageFromStorage(deck.deck_image, deck.universal_id + "_image.png"));
        }
    }
}
